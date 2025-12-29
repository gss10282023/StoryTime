package com.storytime.service;

import com.storytime.model.VideoGenerationRequest;
import com.storytime.model.VideoGenerationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class LumaService {

    private static final String LUMA_API_URL = "https://api.lumalabs.ai/dream-machine/v1/generations";
    private final RestTemplate restTemplate;

    @Value("${storytime.demo:false}")
    private boolean demoMode;

    @Value("${storytime.demo.video-url:/demo/demo.mp4}")
    private String demoVideoUrl;

    @Value("${luma.api.key}")
    String lumaApiKey;

    @Autowired
    public LumaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public VideoGenerationResponse createGeneration(VideoGenerationRequest request) {
        if (demoMode) {
            VideoGenerationResponse demo = new VideoGenerationResponse();
            demo.setId("demo-" + UUID.randomUUID());
            demo.setState("completed");
            VideoGenerationResponse.Assets assets = new VideoGenerationResponse.Assets();
            assets.setVideo(demoVideoUrl);
            demo.setAssets(assets);
            return demo;
        }

        // 检查 API Key
        if (lumaApiKey == null || lumaApiKey.isEmpty()) {
            throw new IllegalStateException("API Key is missing or not configured.");
        }

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(lumaApiKey);

        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", request.getPrompt());

        // 添加 keyframes，构建为 Map
        Map<String, Object> keyframes = new HashMap<>();
        Map<String, Object> frame0 = new HashMap<>();
        frame0.put("type", "image");
        frame0.put("url", request.getImageUrl());
        keyframes.put("frame0", frame0);
        requestBody.put("keyframes", keyframes);

        // 打印请求体以便调试
        System.out.println("Request Body: " + requestBody);

        // 设置其他参数（可选）
        requestBody.put("loop", false);
        requestBody.put("aspect_ratio", "16:9");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 发送 POST 请求
            ResponseEntity<VideoGenerationResponse> response = restTemplate.postForEntity(
                    LUMA_API_URL,
                    entity,
                    VideoGenerationResponse.class
            );

            // 检查响应体
            VideoGenerationResponse videoResponse = response.getBody();
            if (videoResponse == null) {
                throw new RuntimeException("API returned an empty response.");
            }

            return videoResponse;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // 捕获 HTTP 异常并重新抛出自定义异常
            System.err.println("Error during API call: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            throw new RuntimeException("API request failed", e);
        }
    }

    public VideoGenerationResponse getGenerationStatus(String id) {
        if (demoMode) {
            VideoGenerationResponse demo = new VideoGenerationResponse();
            demo.setId(id);
            demo.setState("completed");
            VideoGenerationResponse.Assets assets = new VideoGenerationResponse.Assets();
            assets.setVideo(demoVideoUrl);
            demo.setAssets(assets);
            return demo;
        }

        // 检查 API Key
        if (lumaApiKey == null || lumaApiKey.isEmpty()) {
            throw new IllegalStateException("API Key is missing or not configured.");
        }

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(lumaApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // 发送 GET 请求
            ResponseEntity<VideoGenerationResponse> response = restTemplate.exchange(
                    LUMA_API_URL + "/" + id,
                    HttpMethod.GET,
                    entity,
                    VideoGenerationResponse.class
            );

            // 检查响应体
            VideoGenerationResponse videoResponse = response.getBody();
            if (videoResponse == null) {
                throw new RuntimeException("API returned an empty response.");
            }

            return videoResponse;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // 捕获 HTTP 异常并重新抛出自定义异常
            System.err.println("Error during API call: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            throw new RuntimeException("API request failed", e);
        }
    }
}
