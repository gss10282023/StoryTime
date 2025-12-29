// package: group12.storytime.service

package group12.storytime.service;

import group12.storytime.entity.GenerationLog;
import group12.storytime.entity.PrivateFairyTale;
import group12.storytime.entity.StoryImage;
import group12.storytime.entity.Video;
import group12.storytime.model.*;
import group12.storytime.config.S3Config;
import group12.storytime.repository.GenerationLogMapper;
import group12.storytime.repository.PrivateFairyTaleMapper;

import group12.storytime.repository.StoryImageMapper;
import group12.storytime.repository.VideoMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.Base64;
import java.util.UUID;

/**
 * FairytaleService 类负责生成童话故事的各个部分，包括故事文本、图像、音频和视频。
 * 它还通过 WebSocket 将生成的进度和结果推送给客户端。
 */
@Service
public class FairytaleService {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private StableDiffusionService stableDiffusionService;

    @Autowired
    private AudioService audioService;

    @Autowired
    private LumaService lumaService;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3Config s3Config;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;  // 用于发送 WebSocket 消息



    @Autowired
    private GenerationLogMapper generationLogMapper;

    @Autowired
    private PrivateFairyTaleMapper privateFairytaleMapper;

    @Autowired
    private StoryImageMapper storyImageMapper;

    @Autowired
    private VideoMapper videoMapper;


    private static final UUID USER_ID = UUID.fromString("47377ed3-2f5a-43b7-954b-564a8891e8f5");

    /**
     * 异步方法，生成童话故事并推送进度和结果给客户端。
     *
     * @param request   用户的请求参数
     * @param sessionId 当前会话的 ID，用于标识 WebSocket 通道
     */
    @Async  // 标记方法为异步执行
    @Transactional
    public void generateFairytale(FairytaleRequest request, String sessionId) {
        try {
            // 向客户端发送开始生成的消息
            messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Starting story generation...");
            System.out.println("Starting story generation...");

            // 获取特征字符串
            String characteristic = request.getCharacteristic();

            // 构建优化后的故事生成 prompt
            String storyPrompt = String.format(
                    "Now you will help me generate a fairy tale. First, I will provide you with the user's age group, user gender, and story characteristic. Then follow the steps I provide next to complete the fairy tale generation.\n\n" +
                            "User's age group is %s, user gender is %s, story characteristic is '%s'.\n\n" +
                            "Please read the following steps carefully, do not make any mistakes.\n\n" +
                            "Step 1: The protagonists are real-world people, use their names directly. If you generate male protagonists, choose between Matt Damon or Leonardo DiCaprio; if female characters, choose between Emma Watson or Margot Robbie.\n\n" +
                            "Step 2: In the generated fairy tale, there are 2 protagonists, only the selected characters above, and no other animals or creatures can appear.\n\n" +
                            "Step 3: Each protagonist has their own unique hairstyle and outfit (e.g., blue dress, metal armor).\n\n" +
                            "Step 4: The characteristic provided by the user needs to be linked to specific protagonists. If there is not enough characteristic, you decide the protagonists' characteristics based on the story content.\n\n" +
                            "Step 5: This is a short fairy tale, divided into 4 scenes according to the beginning, development, climax, and ending of the story. Each scene only has 2-3 short sentences.\n\n" +
                            "Step 6: In each scene, you need to repeatedly mention the protagonists' hairstyles, clothing, and characteristics.\n\n" +
                            "Step 7: Upon completing the story generation, please check again whether it meets the steps. If it meets the steps, then provide the story to me.\n\n" +
                            "Step 8: Output all scenes together, only output this text, no other information or serial numbers are needed.",
                    request.getAgeGroup(), request.getGender(), characteristic
            );

            // 调用 OpenAI 服务生成故事
            String fairyTale = openAIService.generateStory(storyPrompt);

            messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Story generated.");
            System.out.println("Story generated.");

            // 打印并保存生成的故事内容
            System.out.println("生成的故事内容：\n" + fairyTale);


            UUID privateFairytaleId = UUID.randomUUID();
            String title = fairyTale.substring(0, Math.min(5, fairyTale.length()));
            String audioURL = "https:/207.148.83.235/audio/" + UUID.randomUUID();

            PrivateFairyTale privateFairyTale = new PrivateFairyTale();
            privateFairyTale.setPrivateFairytaleID(privateFairytaleId);
            privateFairyTale.setUserID(USER_ID);
            privateFairyTale.setTitle(title);
            privateFairyTale.setContent(fairyTale);
            privateFairyTale.setLanguage("english");
            privateFairyTale.setAudioURL(audioURL);

            privateFairytaleMapper.insertPrivateFairyTale(privateFairyTale);



            // 构建特定内容的 prompt，用于提取场景描述
            String specificContentPrompt =
                    "Please summarize the following story, no need for serial numbers. Use the format: Who, with what hairstyle, wearing what clothes, having what expression, is doing what where (or with whom, with what hairstyle, wearing what clothes, having what expression).\n\n" +
                            "Note: 'What hairstyle', 'wearing what clothes', and 'having what expression' should remain consistent throughout all paragraphs. Also, use full names. Repeat the location in each paragraph.\n\n" +
                            "This is someone else's story template, please refer to:\n\n" +
                            "Natalie Portman, with long flowing hair and a bright blue cape, stands at the forest entrance, feeling unsure. Scarlett Johansson, with a high blonde ponytail and silver armor, confidently stands beside her.\n\n" +
                            "Natalie Portman, with her long flowing hair and bright blue cape, begins to regain courage. Scarlett Johansson, with her high blonde ponytail and silver armor, leads the way confidently. They enter the forest.\n\n" +
                            "Natalie Portman, with her long flowing hair and bright blue cape, feels fear. Scarlett Johansson, with her high blonde ponytail and silver armor, steps forward without hesitation. They reach a dark cave.\n\n" +
                            "Natalie Portman, with her long flowing hair and bright blue cape, finally finds her courage and follows Scarlett Johansson. Scarlett Johansson, with her high blonde ponytail and silver armor, leads them, and they both emerge victorious and brave.\n\n" +
                            "Here is the story:\n" + fairyTale;

            // 调用 OpenAI 服务生成特定内容
            String specificContent = openAIService.generateDescription(specificContentPrompt);

            messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Descriptions generated.");
            System.out.println("Descriptions generated.");

            // 打印特定内容
            System.out.println("生成的特定内容：\n" + specificContent);

            // 将特定内容按段落拆分，每个段落对应一个场景
            String[] storyParagraphs = fairyTale.split("\n\n");
            String[] scenes = specificContent.split("\n\n");

            // 使用相同的 Seed 确保每次生成的图片一致
            int seed = 12345;

            for (int i = 0; i < scenes.length; i++) {
                try {
                    messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Generating image for scene " + (i + 1));
                    System.out.println("Generating image for scene " + (i + 1));

                    // 构建图片生成的请求
                    GenerationRequest generationRequest = new GenerationRequest();
                    generationRequest.setPrompt(scenes[i]);             // 使用场景描述作为提示词
                    generationRequest.setModel("sd3-large");            // 使用 sd3-large 模型
                    generationRequest.setAspectRatio("16:9");           // 设置长宽比为 16:9
                    generationRequest.setSeed(seed);                    // 设置种子数

                    // 调用 Stable Diffusion 服务生成图片
                    byte[] imageBytes = stableDiffusionService.generateImage(generationRequest);

                    messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Image generated for scene " + (i + 1));
                    System.out.println("Image generated for scene " + (i + 1));

                    // 将图片转换为 Base64 编码的字符串，供前端显示
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                    // 生成音频（语音朗读每个段落）
                    messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Generating audio for scene " + (i + 1));
                    System.out.println("Generating audio for scene " + (i + 1));

                    byte[] audioBytes = audioService.generateAudio(storyParagraphs[i]);
                    String base64Audio = Base64.getEncoder().encodeToString(audioBytes);

                    messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Audio generated for scene " + (i + 1));
                    System.out.println("Audio generated for scene " + (i + 1));

                    // 创建场景数据
                    Map<String, String> sceneData = new HashMap<>();
                    sceneData.put("image", base64Image);
                    sceneData.put("text", storyParagraphs[i]);
                    sceneData.put("audio", base64Audio);
                    sceneData.put("sceneIndex", String.valueOf(i));

                    // 如果用户选择了生成视频，并且是第一张图片
                    if (request.isGenerateVideo() && i == 0) {
                        messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Uploading image for video generation...");
                        System.out.println("Uploading image for video generation...");

                        // 将图片上传到对象存储，获取 URL
                        String imageUrl = uploadImageToS3(imageBytes);

                        messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Image uploaded for video generation.");
                        System.out.println("Image uploaded for video generation.");




                        UUID imageId = UUID.randomUUID();

                        StoryImage storyImage = new StoryImage();
                        storyImage.setImageID(imageId);
                        storyImage.setPrivateFairytaleID(privateFairytaleId);
                        storyImage.setPublicFairytaleID(null);
                        storyImage.setImageURL(imageUrl);

                        storyImageMapper.insertStoryImage(storyImage);





                        // 创建视频生成请求
                        VideoGenerationRequest videoRequest = new VideoGenerationRequest();
                        videoRequest.setPrompt(scenes[i]);
                        videoRequest.setImageUrl(imageUrl);

                        messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Starting video generation...");
                        System.out.println("Starting video generation...");

                        // 调用 LumaService 生成视频
                        VideoGenerationResponse videoResponse = lumaService.createGeneration(videoRequest);
                        String videoGenerationId = videoResponse.getId();

                        // 异步检查视频生成状态
                        checkVideoStatus(videoGenerationId, sessionId, sceneData);
                    } else {
                        sceneData.put("videoUrl", "");
                    }

                    // 将场景数据发送给客户端
                    messagingTemplate.convertAndSend("/topic/scene/" + sessionId, sceneData);

                } catch (Exception e) {
                    e.printStackTrace();
                    messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Error generating scene " + (i + 1));
                    System.out.println("Error generating scene " + (i + 1));

                    // 处理错误，可以在前端显示默认图片或错误信息
                    Map<String, String> sceneData = new HashMap<>();
                    sceneData.put("image", ""); // 空字符串表示没有图片
                    sceneData.put("text", storyParagraphs[i] + "\n(图片生成失败)");
                    sceneData.put("audio", ""); // 空字符串表示没有音频
                    sceneData.put("videoUrl", ""); // 空字符串表示没有视频
                    sceneData.put("sceneIndex", String.valueOf(i));

                    // 将错误的场景数据发送给客户端
                    messagingTemplate.convertAndSend("/topic/scene/" + sessionId, sceneData);
                }
            }

            messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "All scenes generated.");
            System.out.println("All scenes generated.");

        } catch (Exception e) {
            e.printStackTrace();
            messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Error generating fairytale.");
            System.out.println("Error generating fairytale.");
        }
    }

    /**
     * 异步检查视频生成状态的方法，并在生成完成后将视频 URL 发送给客户端。
     *
     * @param generationId 视频生成任务的 ID
     * @param sessionId    当前会话的 ID，用于标识 WebSocket 通道
     * @param sceneData    场景数据，用于更新视频 URL
     */
    void checkVideoStatus(String generationId, String sessionId, Map<String, String> sceneData) {
        new Thread(() -> {
            try {
                while (true) {
                    VideoGenerationResponse statusResponse = lumaService.getGenerationStatus(generationId);
                    String status = statusResponse.getState();

                    messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Video generation status: " + status);
                    System.out.println("Video generation status: " + status);

                    if ("completed".equalsIgnoreCase(status)) {
                        // 获取视频 URL
                        String resultUrl = statusResponse.getAssets().getVideo(); // 修改这里
                        sceneData.put("videoUrl", resultUrl);
                        // 将更新后的场景数据发送给客户端
                        messagingTemplate.convertAndSend("/topic/scene/" + sessionId, sceneData);



                        UUID videoId = UUID.randomUUID();

                        Video video = new Video();
                        video.setVideoID(videoId);
                        video.setUserID(USER_ID);
                        video.setCreationDate(new Timestamp(System.currentTimeMillis()));
                        video.setSubject("English"); // 将 Subject 修改为 "English"
                        video.setAgeGroup(1);
                        video.setMainCharacter("Generated by Ai");
                        video.setVideoURL(resultUrl); // 使用实际的视频 URL
                        video.setLanguage("English");

                        videoMapper.insertVideo(video);



                        UUID logId = UUID.randomUUID();


                        GenerationLog generationLog = new GenerationLog();
                        generationLog.setLogID(logId);
                        generationLog.setUserID(USER_ID);
                        generationLog.setVideoID(videoId);
                        generationLog.setTimestamp(new Timestamp(System.currentTimeMillis()));

                        generationLogMapper.insertGenerationLog(generationLog);


                        break;  // 退出循环，避免重复检查
                    } else if ("failed".equalsIgnoreCase(status)) {
                        messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Video generation failed.");
                        System.out.println("Video generation failed.");
                        break;  // 退出循环
                    } else {
                        Thread.sleep(5000); // 每隔5秒检查一次状态
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Error checking video generation status.");
                System.out.println("Error checking video generation status.");
            }
        }).start();
    }

    /**
     * 上传图片到对象存储（例如 AWS S3），返回图片的 URL。
     *
     * @param imageBytes 图片的字节数组
     * @return 图片的公开访问 URL
     * @throws IOException
     */
    String uploadImageToS3(byte[] imageBytes) throws IOException {
        // 生成唯一的图片文件名
        String key = "images/" + UUID.randomUUID() + ".png";
        String bucketName = s3Config.getBucketName();

        // 创建 PutObjectRequest，设置文件公开访问
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)  // 设置文件公开访问
                .contentType("image/png")
                .build();

        // 上传图片到 S3
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageBytes));

        // 获取公开的图片 URL
        String imageUrl = s3Client.utilities()
                .getUrl(builder -> builder.bucket(bucketName).key(key))
                .toExternalForm();

        return imageUrl;
    }
}