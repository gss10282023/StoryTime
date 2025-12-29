package com.storytime;

import com.storytime.service.OpenAIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OpenAIServiceTest {

    @Autowired
    private OpenAIService openAIService;

    @Test
    public void testGenerateStory() {
        String prompt = "Tell me a story about a brave knight.";
        String response = openAIService.generateStory(prompt);

        assertNotNull(response, "Response should not be null");
        assertFalse(response.isEmpty(), "Response should not be empty");
        System.out.println("Generated story: " + response);
    }

    @Test
    public void testGenerateDescription() {
        String prompt = "Describe a beautiful sunset over the ocean.";
        String response = openAIService.generateDescription(prompt);

        assertNotNull(response, "Response should not be null");
        assertFalse(response.isEmpty(), "Response should not be empty");
        System.out.println("Generated description: " + response);
    }
}