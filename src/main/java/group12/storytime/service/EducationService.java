// package: group12.storytime.service

package group12.storytime.service;

import group12.storytime.entity.MathExercise;
import group12.storytime.entity.Video;
import group12.storytime.model.*;
import group12.storytime.config.S3Config;
import group12.storytime.repository.GenerationParametersMapper;
import group12.storytime.repository.MathExerciseMapper;
import group12.storytime.repository.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
 * EducationService 类负责生成教育内容的各个部分，包括教育句子、图像、音频和视频。
 * 它还通过 WebSocket 将生成的进度和结果推送给客户端。
 */
@Service
public class EducationService {

    UUID exerciseId = null;

    private UUID ExerciseID;
    private UUID VideoID;
    private String Question;
    private String Answer;

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
    private VideoMapper videoMapper;

    @Autowired
    private MathExerciseMapper mathExerciseMapper;

    @Autowired
    private GenerationParametersMapper generationParametersMapper;


    private static final UUID USER_ID = UUID.fromString("47377ed3-2f5a-43b7-954b-564a8891e8f5");

    /**
     * 异步方法，生成教育内容并推送进度和结果给客户端。
     *
     * @param request   用户的请求参数
     * @param sessionId 当前会话的 ID，用于标识 WebSocket 通道
     */
    @Async
    @Transactional// 标记方法为异步执行
    public void generateEducationContent(EducationRequest request, String sessionId) {
        try {
            // 向客户端发送开始生成的消息
            messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Starting education content generation...");
            System.out.println("Starting education content generation...");

            // 构建优化后的 Prompt
            String initialPrompt = String.format(
                    "Now you will help me generate an educational sentence. This sentence will be used for children's educational purposes. First, I will provide you with the character, user age, subject, and the information that the user wants to learn (this input information may also be absent). With this information, I will provide you with specific steps, and then you generate.\n\n" +
                            "Character is %s, age is %s, subject is %s, user's specific learning information input is '%s'.\n\n" +
                            "Please read the following steps carefully, do not make any mistakes.\n" +
                            "Step one: This is a short educational sentence, only one short sentence. For example, if the user selects math: '3 + 2 = ?' or 'The little rabbit has 3 apples and gets 2 more, how many apples does it have now? 3 + 2 = ?'. The math problem must be in mathematical form, not word form.\n" +
                            "Another example is if the user selects English: 'A cute rabbit is eating a carrot. It looks happy.'\n" +
                            "Step two: Upon completing the sentence generation, please check again whether it meets the steps. If it meets the steps, then provide the sentence to me.\n" +
                            "Step three: Only output this sentence, do not need any other information or numbers.",
                    request.getMainCharacter(), request.getAgeGroup(), request.getSubject(), request.getUserInput()
            );

            // 调用 OpenAI 服务生成教育句子
            String educationSentence = openAIService.generateStory(initialPrompt);

            messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Education sentence generated.");
            System.out.println("Education sentence generated.");

            // 打印生成的教育句子
            System.out.println("生成的教育句子：\n" + educationSentence);










            // 提炼信息的 Prompt
            String extractionPrompt = "";

            if ("Math".equalsIgnoreCase(request.getSubject())) {
                extractionPrompt = "Now I need to extract the above sentence. If it is a math problem, I need: Description, Question, Answer, Feedback. The math problem must be in mathematical form, not word form. An example is: 'The little rabbit has 3 apples and gets 2 more, how many apples does it have now? 3 + 2 = ?'\n" +
                        "Description: 'The little rabbit has 3 apples and gets 2 more.'\n" +
                        "Question: '3 + 2 = ?'\n" +
                        "Answer: '5'\n" +
                        "Feedback: 'This is a simple math problem.'\n" +
                        "Please output the extracted information, output line by line. Do not include other information (such as titles like Description:, Word:, Question:, Answer:, Feedback:) and numbers.\n\n" +
                        "Here is the sentence: " + educationSentence;
            } else if ("English".equalsIgnoreCase(request.getSubject())) {
                extractionPrompt = "Now I need to extract the above sentence. If it is an English problem, I need: Word, Description. An example is: 'A cute rabbit is eating a carrot. It looks happy.'\n" +
                        "Word: 'Carrot'\n" +
                        "Description: 'A cute rabbit is eating a carrot. It looks happy.'\n" +
                        "Please output the extracted information, output line by line. Do not include other information (such as titles like Description:, Word:) and numbers.\n\n" +
                        "Here is the sentence: " + educationSentence;
            }

            // 调用 OpenAI 服务提取信息
            String extractedInfo = openAIService.generateDescription(extractionPrompt);

            messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Information extracted.");
            System.out.println("Information extracted.");

            // 打印提取的信息
            System.out.println("提取的信息：\n" + extractedInfo);



            if ("Math".equalsIgnoreCase(request.getSubject())) {
                this.exerciseId = UUID.randomUUID();

                // 从提取的信息中获取 Question 和 Answer
                String[] extractedLines = extractedInfo.split("\n");
                String question = "";
                String answer = "";

                if (extractedLines.length >= 3) {
                    question = extractedLines[1]; // 第二行是 Question
                    answer = extractedLines[2];   // 第三行是 Answer
                }

                this.Question = question;
                this.Answer = answer;


            }

            // 从提取的信息中获取 Description（针对数学和英语两种情况）
            String description = extractDescription(extractedInfo, request.getSubject());

            // 生成图像
            messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Generating image...");
            System.out.println("Generating image...");

            String base64Image = "";
            byte[] imageBytes = null;
            try {
                GenerationRequest generationRequest = new GenerationRequest();
                generationRequest.setPrompt(description);      // 使用描述作为提示词
                generationRequest.setModel("sd3-large");       // 使用 sd3-large 模型
                generationRequest.setAspectRatio("16:9");      // 设置长宽比为 16:9

                imageBytes = stableDiffusionService.generateImage(generationRequest);

                messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Image generated.");
                System.out.println("Image generated.");

                base64Image = Base64.getEncoder().encodeToString(imageBytes);

            } catch (Exception e) {
                e.printStackTrace();
                messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Error generating image.");
                System.out.println("Error generating image.");
                // 处理错误，可以在前端显示默认图片或错误信息
                base64Image = ""; // 空字符串表示没有图片
            }

            // 生成音频（语音朗读整个教育句子）
            messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Generating audio...");
            System.out.println("Generating audio...");

            byte[] audioBytes = audioService.generateAudio(educationSentence);
            String base64Audio = Base64.getEncoder().encodeToString(audioBytes);

            messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Audio generated.");
            System.out.println("Audio generated.");

            // 创建结果数据
            Map<String, String> resultData = new HashMap<>();
            resultData.put("educationSentence", educationSentence);
            resultData.put("extractedInfo", extractedInfo);
            resultData.put("image", base64Image);
            resultData.put("audio", base64Audio);
            resultData.put("videoUrl", "");

            // 如果用户选择了生成视频
            if (request.isGenerateVideo() && imageBytes != null) {
                messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Uploading image for video generation...");
                System.out.println("Uploading image for video generation...");

                // 将图片上传到对象存储，获取 URL
                String imageUrl = uploadImageToS3(imageBytes);

                messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Image uploaded for video generation.");
                System.out.println("Image uploaded for video generation.");

                // 创建视频生成请求
                VideoGenerationRequest videoRequest = new VideoGenerationRequest();
                videoRequest.setPrompt(description);
                videoRequest.setImageUrl(imageUrl);

                messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Starting video generation...");
                System.out.println("Starting video generation...");

                // 调用 LumaService 生成视频
                VideoGenerationResponse videoResponse = lumaService.createGeneration(videoRequest);
                String videoGenerationId = videoResponse.getId();

                // 异步检查视频生成状态
                checkVideoStatus(videoGenerationId, sessionId, resultData);
            } else {
                // 未选择生成视频，或者图片生成失败
                resultData.put("videoUrl", "");
                // 将结果数据发送给客户端
                messagingTemplate.convertAndSend("/topic/result/" + sessionId, resultData);
            }

            // 如果未选择生成视频，直接发送结果数据
            if (!request.isGenerateVideo()) {
                // 将结果数据发送给客户端
                messagingTemplate.convertAndSend("/topic/result/" + sessionId, resultData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Error generating education content.");
            System.out.println("Error generating education content.");
        }
    }

    /**
     * 异步检查视频生成状态的方法，并在生成完成后将视频 URL 发送给客户端。
     *
     * @param generationId 视频生成任务的 ID
     * @param sessionId    当前会话的 ID，用于标识 WebSocket 通道
     * @param resultData   结果数据，用于更新视频 URL
     */
    void checkVideoStatus(String generationId, String sessionId, Map<String, String> resultData) {
        new Thread(() -> {
            try {
                while (true) {
                    VideoGenerationResponse statusResponse = lumaService.getGenerationStatus(generationId);
                    String status = statusResponse.getState();

                    messagingTemplate.convertAndSend("/topic/progress/" + sessionId, "Video generation status: " + status);
                    System.out.println("Video generation status: " + status);

                    if ("completed".equalsIgnoreCase(status)) {
                        // 获取视频 URL
                        String videoUrl = statusResponse.getAssets().getVideo(); // 根据您的 API，获取视频 URL
                        resultData.put("videoUrl", videoUrl);
                        // 将结果数据发送给客户端
                        messagingTemplate.convertAndSend("/topic/result/" + sessionId, resultData);


                        UUID videoId = UUID.randomUUID();

                        Video video = new Video();
                        video.setVideoID(videoId);
                        video.setUserID(USER_ID);
                        video.setCreationDate(new Timestamp(System.currentTimeMillis()));
                        video.setSubject("Math");
                        video.setAgeGroup(1); // 填入 1
                        video.setMainCharacter("Generated by Ai");
                        video.setVideoURL(videoUrl); // 假的 VideoURL
                        video.setLanguage("English"); // 根据您的要求，填入 "Math" 或 "English"

                        videoMapper.insertVideo(video);


                        MathExercise mathExercise = new MathExercise();
                        mathExercise.setExerciseID(exerciseId);
                        mathExercise.setVideoID(videoId);
                        mathExercise.setQuestion(Question);
                        mathExercise.setAnswer(Answer);
                        mathExercise.setFeedback(null);


                        mathExerciseMapper.insertMathExercise(mathExercise);

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

    // 上传图片到对象存储，返回图片的 URL
    private String uploadImageToS3(byte[] imageBytes) throws IOException {
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

    /**
     * 从提取的信息中获取 Description。
     *
     * @param extractedInfo 提取的信息字符串
     * @param subject       科目（Math 或 English）
     * @return Description 字符串
     */
    private String extractDescription(String extractedInfo, String subject) {
        String description = "";
        String[] lines = extractedInfo.split("\n");
        if ("Math".equalsIgnoreCase(subject)) {
            // 对于数学题，第一行是 Description
            if (lines.length > 0) {
                description = lines[0]; // 获取第一行作为描述
            }
        } else if ("English".equalsIgnoreCase(subject)) {
            // 对于英语，第二行是 Description
            if (lines.length > 1) {
                description = lines[1]; // 获取第二行作为描述
            }
        }
        return description;
    }
}
