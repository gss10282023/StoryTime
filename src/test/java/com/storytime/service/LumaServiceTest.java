package com.storytime.service;

import com.storytime.model.VideoGenerationRequest;
import com.storytime.model.VideoGenerationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LumaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LumaService lumaService;

    @Value("${luma.api.key}")
    private String lumaApiKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lumaService.lumaApiKey = "mock-api-key";
    }

    @Test
    void testCreateGeneration_Success() {
        // 设置测试请求
        VideoGenerationRequest request = new VideoGenerationRequest();
        request.setPrompt("Generate a video");
        request.setImageUrl("http://example.com/image.png");

        // 模拟成功响应
        VideoGenerationResponse mockResponse = new VideoGenerationResponse();
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(VideoGenerationResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // 调用 createGeneration 方法
        VideoGenerationResponse response = lumaService.createGeneration(request);

        // 验证结果
        assertNotNull(response);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(VideoGenerationResponse.class));

        // 捕获请求体
        ArgumentCaptor<HttpEntity> httpEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).postForEntity(eq("https://api.lumalabs.ai/dream-machine/v1/generations"), httpEntityCaptor.capture(), eq(VideoGenerationResponse.class));
        System.out.println("Captured request body: " + httpEntityCaptor.getValue().getBody());
    }

    @Test
    void testCreateGeneration_ApiError() {
        // 设置测试请求
        VideoGenerationRequest request = new VideoGenerationRequest();
        request.setPrompt("Generate a video");
        request.setImageUrl("http://example.com/image.png");

        // 模拟 API 错误响应
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(VideoGenerationResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN, "Forbidden"));

        // 调用 createGeneration 方法并验证异常
        Exception exception = assertThrows(RuntimeException.class, () -> lumaService.createGeneration(request));
        assertEquals("API request failed", exception.getMessage());
    }

    @Test
    void testGetGenerationStatus_Success() {
        // 设置测试 ID
        String id = "mock-id";

        // 模拟成功响应
        VideoGenerationResponse mockResponse = new VideoGenerationResponse();
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(VideoGenerationResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // 调用 getGenerationStatus 方法
        VideoGenerationResponse response = lumaService.getGenerationStatus(id);

        // 验证结果
        assertNotNull(response);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(VideoGenerationResponse.class));
    }

    @Test
    void testGetGenerationStatus_ApiError() {
        // 设置测试 ID
        String id = "mock-id";

        // 模拟 RestTemplate 抛出 HttpClientErrorException
        HttpClientErrorException forbiddenException = new HttpClientErrorException(HttpStatus.FORBIDDEN, "Forbidden");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(VideoGenerationResponse.class)))
                .thenThrow(forbiddenException);

        // 调用 getGenerationStatus 方法并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> lumaService.getGenerationStatus(id));
        assertEquals("API request failed", exception.getMessage());
    }

}
