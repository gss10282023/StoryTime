package com.storytime.service;

import com.storytime.model.*;
import com.storytime.config.S3Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EducationServiceTest {

    @Mock
    private OpenAIService openAIService;

    @Mock
    private StableDiffusionService stableDiffusionService;

    @Mock
    private AudioService audioService;

    @Mock
    private LumaService lumaService;

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Config s3Config;

    @Mock
    private S3Utilities s3Utilities;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private EducationService educationService;

    private final CountDownLatch latch = new CountDownLatch(1);

    @BeforeEach
    void setUp() throws IOException, ApiException {
        MockitoAnnotations.openMocks(this);

        // 模拟 OpenAI 服务返回有效数据
        when(openAIService.generateStory(anyString())).thenReturn("3 + 2 = ?");
        when(openAIService.generateDescription(anyString())).thenReturn("Description\n3 + 2 = ?\n5\nFeedback");

        // 模拟 Stable Diffusion 服务返回图像
        when(stableDiffusionService.generateImage(any(GenerationRequest.class))).thenReturn(new byte[10]);

        // 模拟音频生成服务
        when(audioService.generateAudio(anyString())).thenReturn(new byte[10]);

        // 模拟视频生成服务
        VideoGenerationResponse videoResponse = new VideoGenerationResponse();
        videoResponse.setId("videoId");
        videoResponse.setState("completed");
        VideoGenerationResponse.Assets assets = new VideoGenerationResponse.Assets();
        assets.setVideo("videoUrl");
        videoResponse.setAssets(assets);
        when(lumaService.createGeneration(any(VideoGenerationRequest.class))).thenReturn(videoResponse);
        when(lumaService.getGenerationStatus(anyString())).thenReturn(videoResponse);

//        // 模拟 S3 配置和上传
//        when(s3Config.getBucketName()).thenReturn("mock-bucket");
//        when(s3Client.utilities()).thenReturn(s3Utilities);
//
//        // 创建一个有效的 URL 对象
//        URL mockUrl = new URL("https://mock-s3-url.com/image.png");
//        when(s3Utilities.getUrl((Consumer<GetUrlRequest.Builder>) any())).thenReturn(mockUrl);
//
//        // 模拟 putObject 方法
//        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
//                .thenReturn(PutObjectResponse.builder().build());
        // 模拟 S3 配置和上传
        when(s3Config.getBucketName()).thenReturn("mock-bucket");
        when(s3Client.utilities()).thenReturn(s3Utilities);

        // 使用 ArgumentCaptor 捕获 Consumer 参数
        ArgumentCaptor<Consumer<GetUrlRequest.Builder>> captor = ArgumentCaptor.forClass(Consumer.class);
        doAnswer(invocation -> {
            // 捕获并调用传入的 Consumer
            Consumer<GetUrlRequest.Builder> builderConsumer = invocation.getArgument(0);
            GetUrlRequest.Builder builder = GetUrlRequest.builder();
            builderConsumer.accept(builder);

            // 返回模拟的 URL
            return new URL("https://mock-s3-url.com/image.png");
        }).when(s3Utilities).getUrl(captor.capture());
    }

    @Test
    void testGenerateEducationContent_Math_Success() throws Exception {
        EducationRequest request = createRequest("Math", false);

        // 调用异步方法
        educationService.generateEducationContent(request, "testSessionId");

        // 验证服务和消息的调用
        verify(openAIService).generateStory(anyString());
        verify(openAIService).generateDescription(anyString());
        verify(stableDiffusionService).generateImage(any(GenerationRequest.class));
        verify(audioService).generateAudio(anyString());
        verify(messagingTemplate, atLeastOnce()).convertAndSend(anyString(), anyString());
    }

    @Test
    void testGenerateEducationContent_English_Success() throws Exception {
        EducationRequest request = createRequest("English", false);

        when(openAIService.generateStory(anyString())).thenReturn("A cute rabbit is eating a carrot.");
        when(openAIService.generateDescription(anyString())).thenReturn("Carrot\nA cute rabbit is eating a carrot.");

        educationService.generateEducationContent(request, "testSessionId");

        verify(openAIService).generateStory(anyString());
        verify(openAIService).generateDescription(anyString());
        verify(stableDiffusionService).generateImage(any(GenerationRequest.class));
        verify(audioService).generateAudio(anyString());
    }

    @Test
    void testGenerateEducationContent_OpenAIError() throws Exception {
        EducationRequest request = createRequest("Math", false);

        // 模拟 OpenAI 抛出异常
        when(openAIService.generateStory(anyString())).thenThrow(new RuntimeException("OpenAI error"));

        educationService.generateEducationContent(request, "testSessionId");

        verify(messagingTemplate, atLeastOnce()).convertAndSend(contains("/topic/progress/testSessionId"), contains("Error generating education content."));
    }

    @Test
    void testGenerateEducationContent_ImageGenerationFailure() throws Exception {
        EducationRequest request = createRequest("Math", false);

        // 模拟 Stable Diffusion 抛出异常
        when(stableDiffusionService.generateImage(any(GenerationRequest.class))).thenThrow(new IOException("Image generation error"));

        educationService.generateEducationContent(request, "testSessionId");

        verify(messagingTemplate, atLeastOnce()).convertAndSend(contains("/topic/progress/testSessionId"), contains("Error generating image."));
    }

    @Test
    void testGenerateEducationContent_VideoGenerationFailure() throws Exception {
        EducationRequest request = createRequest("Math", true);

        // 模拟视频生成失败
        when(lumaService.createGeneration(any(VideoGenerationRequest.class))).thenThrow(new RuntimeException("Video generation error"));

        new Thread(() -> {
            educationService.generateEducationContent(request, "testSessionId");
            latch.countDown();
        }).start();

        latch.await(5, TimeUnit.SECONDS);

        verify(messagingTemplate, atLeastOnce()).convertAndSend(contains("/topic/progress/testSessionId"), contains("Error generating education content."));
    }



    private EducationRequest createRequest(String subject, boolean generateVideo) {
        EducationRequest request = new EducationRequest();
        request.setMainCharacter("Rabbit");
        request.setAgeGroup("6-8");
        request.setSubject(subject);
        request.setUserInput("Addition");
        request.setGenerateVideo(generateVideo);
        return request;
    }

    @Test
    void testCheckVideoStatus_Completed() throws InterruptedException {
        // 模拟视频生成响应，状态为 "in_progress"
        VideoGenerationResponse inProgressResponse = new VideoGenerationResponse();
        inProgressResponse.setState("in_progress");

        // 模拟视频生成响应，状态为 "completed"
        VideoGenerationResponse completedResponse = new VideoGenerationResponse();
        completedResponse.setState("completed");
        VideoGenerationResponse.Assets assets = new VideoGenerationResponse.Assets();
        assets.setVideo("videoUrl");
        completedResponse.setAssets(assets);

        // 按顺序返回 "in_progress" 和 "completed"
        when(lumaService.getGenerationStatus(anyString()))
                .thenReturn(inProgressResponse)
                .thenReturn(completedResponse);

        // 调用方法并等待检查完成
        new Thread(() -> {
            educationService.checkVideoStatus("generationId", "testSessionId", new HashMap<>());
        }).start();

        // 等待几秒钟以确保线程执行完毕
        Thread.sleep(6000);

        // 验证是否发送了结果消息
        verify(messagingTemplate, atLeastOnce()).convertAndSend(
                eq("/topic/result/testSessionId"),
                Optional.ofNullable(argThat(argument -> argument instanceof Map && ((Map<?, ?>) argument).get("videoUrl").equals("videoUrl")))
        );
    }




    @Test
    void testCheckVideoStatus_Failed() throws Exception {
        // 模拟状态变为失败
        VideoGenerationResponse responseFailed = new VideoGenerationResponse();
        responseFailed.setState("failed");

        when(lumaService.getGenerationStatus("videoId")).thenReturn(responseFailed);

        Map<String, String> resultData = new HashMap<>();
        CountDownLatch latch = new CountDownLatch(1);

        // 启动测试线程
        new Thread(() -> {
            educationService.checkVideoStatus("videoId", "testSessionId", resultData);
            latch.countDown();
        }).start();

        // 等待方法完成
        latch.await(10, TimeUnit.SECONDS);

        // 验证 WebSocket 消息
        verify(messagingTemplate, timeout(1000).atLeastOnce())
                .convertAndSend(contains("/topic/progress/testSessionId"), contains("Video generation status: failed"));

    }

    @Test
    void testCheckVideoStatus_Error() throws Exception {
        String sessionId = "testSessionId";
        String generationId = "videoId";
        Map<String, String> resultData = new HashMap<>();

        // 模拟 getGenerationStatus 抛出异常
        when(lumaService.getGenerationStatus(anyString())).thenThrow(new RuntimeException("Error checking status"));

        // 调用 checkVideoStatus 方法
        new Thread(() -> {
            educationService.checkVideoStatus(generationId, sessionId, resultData);
        }).start();

        // 验证 messagingTemplate 发送了错误消息
        verify(messagingTemplate, timeout(1000).atLeastOnce())
                .convertAndSend(contains("/topic/progress/" + sessionId), contains("Error checking video generation status."));
    }

}
