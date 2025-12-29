package com.storytime.service;

import com.storytime.entity.GenerationLog;
import com.storytime.entity.PrivateFairyTale;
import com.storytime.entity.StoryImage;
import com.storytime.entity.Video;
import com.storytime.model.FairytaleRequest;
import com.storytime.model.GenerationRequest;
import com.storytime.model.VideoGenerationRequest;
import com.storytime.model.VideoGenerationResponse;
import com.storytime.config.S3Config;
import com.storytime.repository.GenerationLogMapper;
import com.storytime.repository.PrivateFairyTaleMapper;
import com.storytime.repository.StoryImageMapper;
import com.storytime.repository.VideoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FairytaleServiceTest {

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
    private S3Utilities s3Utilities; // 模拟 S3Utilities

    @Mock
    private S3Config s3Config;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private GenerationLogMapper generationLogMapper;

    @Mock
    private PrivateFairyTaleMapper privateFairyTaleMapper;

    @Mock
    private StoryImageMapper storyImageMapper;

    @Mock
    private VideoMapper videoMapper;

    @InjectMocks
    private FairytaleService fairytaleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 设置 S3Client 的 utilities() 方法返回 S3Utilities
        when(s3Client.utilities()).thenReturn(s3Utilities);
    }

    @Test
    void testGenerateFairytale() throws Exception {
        // 模拟请求
        FairytaleRequest request = new FairytaleRequest("6-8", "Female", "Alice", "Brave", true);
        String sessionId = "testSession";

        // 模拟 OpenAI 服务返回
        when(openAIService.generateStory(anyString())).thenReturn("This is a short fairytale.");
        when(openAIService.generateDescription(anyString())).thenReturn("Scene descriptions...");

        // 模拟 Stable Diffusion 服务返回
        byte[] imageBytes = new byte[]{1, 2, 3};
        when(stableDiffusionService.generateImage(any())).thenReturn(imageBytes);

        // 模拟 AudioService 返回
        byte[] audioBytes = new byte[]{4, 5, 6};
        when(audioService.generateAudio(anyString())).thenReturn(audioBytes);

        // 模拟 S3 上传行为
        when(s3Config.getBucketName()).thenReturn("test-bucket");
        when(s3Utilities.getUrl((Consumer<GetUrlRequest.Builder>) any())).thenReturn(new URL("https://mock-s3-url.com/image.png"));

        // 执行测试方法
        fairytaleService.generateFairytale(request, sessionId);

        // 验证消息发送和保存行为
        verify(messagingTemplate, atLeastOnce()).convertAndSend(contains("/topic/progress/" + sessionId), anyString());
        verify(messagingTemplate, atLeastOnce()).convertAndSend(contains("/topic/scene/" + sessionId), any(Map.class));

        verify(privateFairyTaleMapper, atLeastOnce()).insertPrivateFairyTale(any(PrivateFairyTale.class));
    }

    @Test
    void testUploadImageToS3() throws IOException {
        byte[] imageBytes = new byte[]{1, 2, 3};
        when(s3Config.getBucketName()).thenReturn("test-bucket");
        when(s3Client.utilities().getUrl((Consumer<GetUrlRequest.Builder>) any())).thenReturn(new URL("https://mock-s3-url.com/image.png"));

        String imageUrl = fairytaleService.uploadImageToS3(imageBytes);

        // 验证 S3 上传行为
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        assertEquals("https://mock-s3-url.com/image.png", imageUrl);
    }

    @Test
    void testCheckVideoStatus() {
        // 模拟视频生成状态检查
        Map<String, String> sceneData = new HashMap<>();
        VideoGenerationResponse videoResponse = new VideoGenerationResponse();
        VideoGenerationResponse.Assets assets = new VideoGenerationResponse.Assets();
        assets.setVideo("https://mock-video-url.com/video.mp4");
        videoResponse.setId("videoGenId");
        videoResponse.setState("completed");
        videoResponse.setAssets(assets);

        when(lumaService.getGenerationStatus(anyString())).thenReturn(videoResponse);

        // 调用 checkVideoStatus 方法
        fairytaleService.checkVideoStatus("videoGenId", "testSession", sceneData);

        // 验证视频状态检查行为
        verify(messagingTemplate, timeout(1000).atLeastOnce()).convertAndSend(contains("/topic/progress/testSession"), anyString());
    }
}
