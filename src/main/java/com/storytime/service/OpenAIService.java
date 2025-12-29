// OpenAIService.java
package com.storytime.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class OpenAIService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${storytime.demo:false}")
    private boolean demoMode;

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
        if (demoMode) {
            return demoResponse(prompt);
        }

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

    private String demoResponse(String prompt) {
        String p = prompt == null ? "" : prompt.toLowerCase(Locale.ROOT);

        if (p.contains("generate a fairy tale")) {
            return String.join("\n\n",
                    "Lina and Kai find a glowing book in a quiet library. The book opens to a map that points to a missing star.",
                    "They follow the map through a moonlit garden, counting clues along the path. Each clue teaches them a small lesson about being patient and curious.",
                    "A gust of wind scatters the star pieces, so Lina and Kai work together to collect them safely. They encourage each other and stay calm when it gets tricky.",
                    "When the last piece clicks into place, the star lights up the sky again. Lina and Kai smile, proud of what they learned and the teamwork they shared."
            );
        }

        if (p.contains("summarize the following story")) {
            return String.join("\n\n",
                    "Lina, with a short bob haircut, wearing a yellow raincoat, smiles as she opens a glowing book in a quiet library. Kai, with curly hair and a blue hoodie, leans in with a curious expression.",
                    "Lina, with her short bob haircut and yellow raincoat, walks through a moonlit garden while pointing at a map. Kai, with curly hair and a blue hoodie, follows closely, looking amazed at the sparkling clues.",
                    "Lina, with her short bob haircut and yellow raincoat, reaches for a floating star piece with a focused expression. Kai, with curly hair and a blue hoodie, carefully gathers another piece nearby, staying determined.",
                    "Lina, with her short bob haircut and yellow raincoat, laughs with relief as the star shines bright above them. Kai, with curly hair and a blue hoodie, smiles proudly beside her under the glowing sky."
            );
        }

        if (p.contains("extract the above sentence") && p.contains("math problem")) {
            return String.join("\n",
                    "A little rabbit counts apples in a sunny garden.",
                    "3 + 2 = ?",
                    "5",
                    "Great job! You added them correctly."
            );
        }

        if (p.contains("extract the above sentence") && p.contains("english problem")) {
            return String.join("\n",
                    "Carrot",
                    "A cute rabbit is eating a carrot. It looks happy."
            );
        }

        if (p.contains("generate an educational sentence") && p.contains("subject is math")) {
            return "3 + 2 = ?";
        }

        if (p.contains("generate an educational sentence") && p.contains("subject is english")) {
            return "A cute rabbit is eating a carrot. It looks happy.";
        }

        return "Demo mode: no external AI calls are made.";
    }
}
