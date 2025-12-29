package group12.storytime;


import group12.storytime.service.OpenAIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FairytaleGenerationIntegrationTest {

    @Autowired
    private OpenAIService openAIService;

    @Test
    public void testGenerateFairytale() {
        // 测试数据
        String name = "Alice";
        int age = 8;
        String gender = "female";
        String superpower = "flying";

        // 构建故事提示
        String prompt = String.format("Create a fairy tale for a child named %s, age %d, gender %s, with the superpower %s.",
                name, age, gender, superpower);

        // 调用 OpenAI 服务生成童话故事
        String fairyTale = openAIService.generateStory(prompt);

        // 验证生成的故事
        assertNotNull(fairyTale, "The response from OpenAI should not be null.");
        assertTrue(fairyTale.length() > 100, "The fairy tale should be longer than 100 characters.");
        assertTrue(fairyTale.toLowerCase().contains("alice"), "The fairy tale should mention the name 'Alice'.");
        //assertTrue(fairyTale.toLowerCase().contains("flying"), "The fairy tale should mention the superpower 'flying'.");

        // 输出生成的童话故事
        System.out.println("Generated Fairytale: " + fairyTale);
    }
}
