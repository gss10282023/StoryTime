package com.storytime;


import com.storytime.service.OpenAIService;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class FairytaleGenerationIntegrationTest {

    @Test
    void testGenerateFairytale() {
        String apiKey = System.getenv("OPENAI_API_KEY");
        Assumptions.assumeTrue(apiKey != null && !apiKey.isBlank(), "OPENAI_API_KEY is not set");

        OpenAIService openAIService = new OpenAIService();
        ReflectionTestUtils.setField(openAIService, "apiKey", apiKey);

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
