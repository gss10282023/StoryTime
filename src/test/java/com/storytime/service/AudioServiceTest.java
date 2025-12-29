package com.storytime.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AudioServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AudioService audioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        audioService = new AudioService(restTemplate, "test-api-key");
    }

    @Test
    void testGenerateAudio_Success() {
        // 模拟 OpenAI API 返回的音频数据
        byte[] mockAudioData = new byte[]{1, 2, 3, 4};

        // 构建模拟的响应
        ResponseEntity<byte[]> mockResponse = new ResponseEntity<>(mockAudioData, HttpStatus.OK);

        // 模拟 RestTemplate 调用返回的响应
        when(restTemplate.exchange(
                eq("https://api.openai.com/v1/audio/speech"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(byte[].class))
        ).thenReturn(mockResponse);

        // 调用 generateAudio 方法
        byte[] result = audioService.generateAudio("Hello World");

        // 验证返回结果
        assertArrayEquals(mockAudioData, result);

        // 验证 RestTemplate 被正确调用
        verify(restTemplate, times(1)).exchange(
                eq("https://api.openai.com/v1/audio/speech"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(byte[].class)
        );
    }

    @Test
    void testGenerateAudio_NullResponse() {
        // 模拟 OpenAI API 返回的 null 响应
        when(restTemplate.exchange(
                eq("https://api.openai.com/v1/audio/speech"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(byte[].class))
        ).thenReturn(null);

        // 调用 generateAudio 方法
        byte[] result = audioService.generateAudio("Hello World");

        // 验证返回结果为空数组
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testGenerateAudio_NonOKStatus() {
        // 模拟 OpenAI API 返回的非 OK 响应
        ResponseEntity<byte[]> mockResponse = new ResponseEntity<>(new byte[0], HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(
                eq("https://api.openai.com/v1/audio/speech"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(byte[].class))
        ).thenReturn(mockResponse);

        // 调用 generateAudio 方法
        byte[] result = audioService.generateAudio("Hello World");

        // 验证返回结果为空数组
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testGenerateAudio_ExceptionThrown() {
        // 模拟 RestTemplate 抛出异常
        when(restTemplate.exchange(
                eq("https://api.openai.com/v1/audio/speech"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(byte[].class))
        ).thenThrow(new RuntimeException("API error"));

        // 调用 generateAudio 方法
        byte[] result = audioService.generateAudio("Hello World");

        // 验证返回结果为空数组
        assertArrayEquals(new byte[0], result);
    }

    @Test
    void testGenerateAudio_EmptyBody() {
        // 模拟 OpenAI API 返回空 body 的响应
        ResponseEntity<byte[]> mockResponse = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://api.openai.com/v1/audio/speech"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(byte[].class))
        ).thenReturn(mockResponse);

        // 调用 generateAudio 方法
        byte[] result = audioService.generateAudio("Hello World");

        // 验证返回结果为空数组
        assertArrayEquals(new byte[0], result);
    }
}
