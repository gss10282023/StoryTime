package group12.storytime;

import group12.storytime.model.GenerationRequest;
import group12.storytime.service.StableDiffusionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StableDiffusionServiceTest {

    @Autowired
    private StableDiffusionService stableDiffusionService;

    @Test
    public void testGenerateImageAndOutputUrl() {
        // 准备测试用的 GenerationRequest 对象
        GenerationRequest request = new GenerationRequest();
        request.setPrompt("A colorful magical forest with unicorns and rainbows");
        request.setOutputFormat("png"); // 确保使用正确的格式

        try {
            // 调用生成图像的方法
            byte[] imageBytes = stableDiffusionService.generateImage(request);

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

