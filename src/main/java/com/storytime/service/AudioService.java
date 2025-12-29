package com.storytime.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AudioService {

    private final RestTemplate restTemplate;
    private final String apiKey;

    @Value("${storytime.demo:false}")
    private boolean demoMode;

    @Value("${storytime.demo.audio-resource:static/demo/demo.wav}")
    private String demoAudioResource;

    // 构造函数注入
    public AudioService(RestTemplate restTemplate, @Value("${openai.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    // OpenAI 音频生成端点
    private static final String AUDIO_API_URL = "https://api.openai.com/v1/audio/speech";

    /**
     * 调用 OpenAI API 生成音频并返回音频数据（字节数组）。
     *
     * @param text 用户输入的文本
     * @return 生成的音频字节数组
     */
    public byte[] generateAudio(String text) {
        if (demoMode) {
            try {
                ClassPathResource resource = new ClassPathResource(demoAudioResource);
                return StreamUtils.copyToByteArray(resource.getInputStream());
            } catch (IOException e) {
                return new byte[0];
            }
        }

        if (apiKey == null || apiKey.isBlank()) {
            return new byte[0];
        }

        // 构建请求体
        Map<String, Object> request = new HashMap<>();
        request.put("model", "tts-1");
        request.put("voice", "nova");
        request.put("input", text);
        request.put("response_format", "mp3");
        request.put("speed", 0.85);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 构建 HttpEntity（包含请求体和请求头）
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            // 发送 POST 请求到 OpenAI API
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    AUDIO_API_URL, HttpMethod.POST, entity, byte[].class);

            // 如果请求成功，返回音频数据
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            // 捕获并打印异常信息
            System.err.println("Error calling OpenAI API: " + e.getMessage());
        }

        // 返回空数组（如果生成音频失败）
        return new byte[0];
    }
}
