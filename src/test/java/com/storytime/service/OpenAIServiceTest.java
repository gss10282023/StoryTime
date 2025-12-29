package com.storytime.service;

import com.storytime.service.OpenAIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OpenAIServiceTest {

    @Autowired
    private OpenAIService openAIService;

    @Mock
    private RestTemplate restTemplate; // Mock RestTemplate

    @InjectMocks
    private OpenAIService mockOpenAIService; // 用于注入 Mock 对象

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 将 mockOpenAIService 的 apiKey 设置为测试值
        ReflectionTestUtils.setField(mockOpenAIService, "apiKey", "test-api-key");
    }

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

    // 新增测试方法：模拟无响应的情况
    @Test
    public void testGenerateStory_NoResponse() {
        // 模拟 RestTemplate 返回 null
        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        String response = mockOpenAIService.generateStory("Tell me a story.");
        assertEquals("No response from OpenAI.", response);
    }

    @Test
    public void testGenerateDescription_NoResponse() {
        // 模拟 RestTemplate 返回 null
        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        String response = mockOpenAIService.generateDescription("Describe a scene.");
        assertEquals("No response from OpenAI.", response);
    }

    // 新增测试方法：模拟异常处理
    @Test
    public void testGenerateStory_ExceptionHandling() {
        // 模拟 RestTemplate 抛出异常
        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenThrow(new RuntimeException("API call failed"));

        String response = mockOpenAIService.generateStory("Tell me a story.");
        assertEquals("No response from OpenAI.", response);
    }

    @Test
    public void testGenerateDescription_ExceptionHandling() {
        // 模拟 RestTemplate 抛出异常
        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenThrow(new RuntimeException("API call failed"));

        String response = mockOpenAIService.generateDescription("Describe a sunset.");
        assertEquals("No response from OpenAI.", response);
    }

    // 新增测试方法：模拟空 choices 列表的情况
    @Test
    public void testGenerateStory_EmptyChoices() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("choices", Collections.emptyList());

        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        String response = mockOpenAIService.generateStory("Tell me a story.");
        assertEquals("No response from OpenAI.", response);
    }

    @Test
    public void testGenerateDescription_EmptyChoices() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("choices", Collections.emptyList());

        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        String response = mockOpenAIService.generateDescription("Describe a scene.");
        assertEquals("No response from OpenAI.", response);
    }

    // 新增测试方法：模拟 choices 中的 message 为 null 的情况
    @Test
    public void testGenerateStory_NullMessage() {
        // 创建一个带有 null 值的响应体
        Map<String, Object> message = new HashMap<>();
        message.put("message", null);

        List<Map<String, Object>> choices = new ArrayList<>();
        choices.add(message);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("choices", choices);

        // 模拟 RestTemplate 返回带有 null message 的响应
        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        String response = mockOpenAIService.generateStory("Tell me a story.");
        assertEquals("No response from OpenAI.", response);
    }


    @Test
    public void testGenerateDescription_NullMessage() {
        // 创建一个带有 null 值的响应体
        Map<String, Object> message = new HashMap<>();
        message.put("message", null);

        List<Map<String, Object>> choices = new ArrayList<>();
        choices.add(message);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("choices", choices);

        // 模拟 RestTemplate 返回带有 null message 的响应
        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        String response = mockOpenAIService.generateDescription("Describe a scene.");
        assertEquals("No response from OpenAI.", response);
    }


    // 新增测试方法：模拟 message 中 content 为 null 的情况
    @Test
    public void testGenerateStory_NullContent() {
        // 构建包含 null 内容的响应体
        Map<String, Object> message = new HashMap<>();
        message.put("content", null);  // 模拟 null 内容

        List<Map<String, Object>> choices = new ArrayList<>();
        choices.add(Map.of("message", message));

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("choices", choices);

        // 模拟 RestTemplate 返回带有 null 内容的响应
        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        String response = mockOpenAIService.generateStory("Tell me a story.");
        assertEquals("No response from OpenAI.", response);
    }


    @Test
    public void testGenerateDescription_NullContent() {
        // 构建包含 null 内容的响应体
        Map<String, Object> message = new HashMap<>();
        message.put("content", null);  // 模拟 null 内容

        List<Map<String, Object>> choices = new ArrayList<>();
        Map<String, Object> choice = new HashMap<>();
        choice.put("message", message);
        choices.add(choice);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("choices", choices);

        // 模拟 RestTemplate 返回带有 null 内容的响应
        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        String response = mockOpenAIService.generateDescription("Describe a scene.");
        assertEquals("No response from OpenAI.", response);
    }
    // 增加的测试：模拟 response.getBody() 为 null 的情况
    @Test
    public void testGenerateStory_NullResponseBody() {
        // 模拟 RestTemplate 返回空的响应体
        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        String response = mockOpenAIService.generateStory("Tell me a story.");
        assertEquals("No response from OpenAI.", response);
    }

    @Test
    public void testGenerateDescription_NullResponseBody() {
        // 模拟 RestTemplate 返回空的响应体
        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        String response = mockOpenAIService.generateDescription("Describe a scene.");
        assertEquals("No response from OpenAI.", response);
    }

    // 增加的测试：模拟 response.getBody().get("choices") 非 List 的情况
    @Test
    public void testGenerateStory_ChoicesNotList() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("choices", "invalid_type");  // 非 List 类型

        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        String response = mockOpenAIService.generateStory("Tell me a story.");
        assertEquals("No response from OpenAI.", response);
    }

    @Test
    public void testGenerateDescription_ChoicesNotList() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("choices", "invalid_type");  // 非 List 类型

        when(restTemplate.postForEntity(
                eq("https://api.openai.com/v1/chat/completions"),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        String response = mockOpenAIService.generateDescription("Describe a scene.");
        assertEquals("No response from OpenAI.", response);
    }







}
