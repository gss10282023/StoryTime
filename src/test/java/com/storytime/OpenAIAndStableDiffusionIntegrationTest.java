package com.storytime;

import com.storytime.model.GenerationRequest;
import com.storytime.service.OpenAIService;
import com.storytime.service.StableDiffusionService;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class OpenAIAndStableDiffusionIntegrationTest {

    @Test
    void testOpenAIAndStableDiffusionIntegration() {
        String openAiKey = System.getenv("OPENAI_API_KEY");
        String stableDiffusionKey = System.getenv("STABLEDIFFUSION_API_KEY");
        Assumptions.assumeTrue(openAiKey != null && !openAiKey.isBlank(), "OPENAI_API_KEY is not set");
        Assumptions.assumeTrue(stableDiffusionKey != null && !stableDiffusionKey.isBlank(), "STABLEDIFFUSION_API_KEY is not set");

        OpenAIService openAIService = new OpenAIService();
        ReflectionTestUtils.setField(openAIService, "apiKey", openAiKey);

        StableDiffusionService stableDiffusionService = new StableDiffusionService(new RestTemplate());
        ReflectionTestUtils.setField(stableDiffusionService, "apiKey", stableDiffusionKey);

        try {
            // 1. 使用 OpenAI 生成关键词或描述
            String openAIPrompt = "Generate 5-7 keywords for a magical forest scene, suitable for a children's story. Separate each keyword with a comma.";
            String description = openAIService.generateDescription(openAIPrompt);

            // 检查 OpenAI 生成的描述不为空
            assertNotNull(description, "OpenAI should return a non-null description");
            assertFalse(description.trim().isEmpty(), "OpenAI description should not be empty");

            // 输出 OpenAI 返回的描述
            System.out.println("OpenAI Generated Description: " + description);

            // 2. 使用生成的描述调用 Stable Diffusion 生成图像
            GenerationRequest generationRequest = new GenerationRequest();
            generationRequest.setPrompt(description);
            generationRequest.setOutputFormat("png"); // 确保输出格式为 png

            byte[] imageBytes = stableDiffusionService.generateImage(generationRequest);

            // 检查生成的图像是否为空
            assertNotNull(imageBytes, "Image should not be null");
            assertTrue(imageBytes.length > 0, "Image should not be empty");

            // 将生成的图像字节数组编码为 base64 字符串，并输出图片的 Base64 URL
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            String imageUrl = "data:image/png;base64," + base64Image;

            // 输出图片的 Base64 URL
            System.out.println("Generated Image Base64 URL: " + imageUrl);

            // 验证图片 URL 是否正确
            assertNotNull(imageUrl, "Image URL should not be null");
            assertTrue(imageUrl.startsWith("data:image/png;base64,"), "Image URL should start with base64 prefix");

        } catch (HttpClientErrorException ex) {
            // 检查是否因内容审核失败
            if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                fail("Content moderation system flagged the request: " + ex.getResponseBodyAsString());
            } else {
                // 处理其他HTTP错误
                fail("HTTP Error: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
            }
        } catch (Exception e) {
            // 捕获并打印任何其他异常
            e.printStackTrace();
            fail("Unexpected error occurred: " + e.getMessage());
        }
    }
}
