// OpenAIService.java
package group12.storytime.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class OpenAIService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${openai.api.key}")
    private String apiKey;
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions"; // 使用聊天模型的端点

    public String generateStory(String prompt) {
        return callOpenAI(prompt, 1000);
    }

    public String generateDescription(String prompt) {
        return callOpenAI(prompt, 500);
    }

    private String callOpenAI(String prompt, int maxTokens) {
        // 构建请求体
        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-4o"); // 使用GPT-4模型
        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "You are a story generator."),
                Map.of("role", "user", "content", prompt)
        );
        request.put("messages", messages);
        request.put("max_tokens", maxTokens);
        request.put("temperature", 0.7); // 可根据需要调整

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey); // 设置Bearer Token进行授权
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_API_URL, entity, Map.class);

            if (response.getBody() != null && response.getBody().get("choices") instanceof List) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    if (choice.get("message") instanceof Map) {
                        Map<String, Object> message = (Map<String, Object>) choice.get("message");
                        return (String) message.get("content");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error calling OpenAI API: " + e.getMessage());
            e.printStackTrace();
        }
        return "No response from OpenAI.";
    }
}
