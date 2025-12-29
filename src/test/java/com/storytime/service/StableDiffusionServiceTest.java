package com.storytime.service;

import com.storytime.model.GenerationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
public class StableDiffusionServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    private StableDiffusionService stableDiffusionService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        // 创建 MockRestServiceServer 并绑定到 RestTemplate
        mockServer = MockRestServiceServer.createServer(restTemplate);
        // 创建 StableDiffusionService 实例并注入 RestTemplate
        stableDiffusionService = new StableDiffusionService(restTemplate);
    }

    @Test
    public void testGenerateImage_Success() throws Exception {
        // 构建请求对象
        GenerationRequest request = new GenerationRequest();
        request.setPrompt("A beautiful sunset over the ocean");

        byte[] fakeImageBytes = new byte[]{1, 2, 3, 4};

        // 模拟成功响应
        mockServer.expect(requestTo("https://api.stability.ai/v2beta/stable-image/generate/sd3"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.IMAGE_PNG)
                        .body(fakeImageBytes));

        // 调用方法
        byte[] result = stableDiffusionService.generateImage(request);

        // 验证结果
        assertNotNull(result);
        assertArrayEquals(fakeImageBytes, result);

        // 验证 MockServer
        mockServer.verify();
    }

    @Test
    public void testGenerateImage_PaymentRequired() {
        // 构建请求对象
        GenerationRequest request = new GenerationRequest();
        request.setPrompt("A cat sitting on a couch");

        // 模拟 402 Payment Required 响应
        mockServer.expect(requestTo("https://api.stability.ai/v2beta/stable-image/generate/sd3"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.PAYMENT_REQUIRED));

        // 验证是否抛出 ApiException
        ApiException thrown = assertThrows(ApiException.class, () -> {
            stableDiffusionService.generateImage(request);
        });

        // 验证异常的状态码
        assertEquals(HttpStatus.PAYMENT_REQUIRED, thrown.getStatusCode());

        // 验证 MockServer
        mockServer.verify();
    }

    @Test
    public void testGenerateImage_InternalServerError() {
        // 构建请求对象
        GenerationRequest request = new GenerationRequest();
        request.setPrompt("A dog playing in the park");

        // 模拟 500 Internal Server Error 响应
        mockServer.expect(requestTo("https://api.stability.ai/v2beta/stable-image/generate/sd3"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // 验证是否抛出 ApiException
        ApiException thrown = assertThrows(ApiException.class, () -> {
            stableDiffusionService.generateImage(request);
        });

        // 验证异常的状态码
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatusCode());

        // 验证 MockServer
        mockServer.verify();
    }

    @Test
    void testGenerateImage_WithAllParameters() throws IOException, ApiException {
        // 构建请求对象
        GenerationRequest request = new GenerationRequest();
        request.setPrompt("test prompt");
        request.setMode("image-to-image");
        request.setModel("sd3-large");
        request.setOutputFormat("png");
        request.setNegativePrompt("no negative");
        request.setAspectRatio("16:9");
        request.setSeed(123);
        request.setStrength(0.8);
        request.setImage(new byte[10]); // 模拟输入图像

        // 模拟 API 响应
        mockServer.expect(ExpectedCount.once(), requestTo("https://api.stability.ai/v2beta/stable-image/generate/sd3"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", org.hamcrest.Matchers.containsString("multipart/form-data"))) // 忽略 boundary
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.IMAGE_PNG)
                        .body(new byte[10])); // 模拟返回的图像数据

        // 调用生成图像方法
        byte[] response = stableDiffusionService.generateImage(request);

        // 验证结果
        assertNotNull(response);
        assertEquals(10, response.length); // 响应长度为10

        // 验证请求匹配
        mockServer.verify();
    }
    @Test
    public void testGenerateImage_WithInvalidApiKey() {
        // 构建请求对象
        GenerationRequest request = new GenerationRequest();
        request.setPrompt("Invalid API key test");

        // 模拟 401 Unauthorized 响应
        mockServer.expect(requestTo("https://api.stability.ai/v2beta/stable-image/generate/sd3"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        // 验证是否抛出 ApiException
        ApiException thrown = assertThrows(ApiException.class, () -> {
            stableDiffusionService.generateImage(request);
        });

        // 验证异常的状态码
        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatusCode());

        // 验证 MockServer
        mockServer.verify();
    }

    @Test
    void testGenerateImage_WithIOException() {
        // 构建请求对象
        GenerationRequest request = new GenerationRequest();
        request.setPrompt("IOException test");

        // 模拟 IOException
        mockServer.expect(requestTo("https://api.stability.ai/v2beta/stable-image/generate/sd3"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(clientHttpRequest -> {
                    throw new IOException("Simulated IO exception");
                });


        // 执行测试并断言抛出 ResourceAccessException
        ResourceAccessException exception = assertThrows(ResourceAccessException.class, () -> {
            stableDiffusionService.generateImage(request);
        });

        // 验证异常的原因是否为 IOException
        assertTrue(exception.getCause() instanceof IOException);
        assertEquals("Simulated IO exception", exception.getCause().getMessage());
    }

}
