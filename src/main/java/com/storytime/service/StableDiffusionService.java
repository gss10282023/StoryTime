//// StableDiffusionService.java
//package com.storytime.service;
//
//import com.storytime.model.GenerationRequest;
//import com.storytime.service.ApiException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.*;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.ResponseErrorHandler;
//import org.springframework.web.client.RestTemplate;
//import java.io.IOException;
//import java.util.Collections;
//
//
///**
// * StableDiffusionService 类用于与 Stable Diffusion API 交互，生成图像。
// */
//@Service
//public class StableDiffusionService {
//
//    // Stable Diffusion API 的 URL
//    private static final String API_URL = "https://api.stability.ai/v2beta/stable-image/generate/sd3";
//
//    // 从配置文件中读取 API 密钥
//    @Value("${stablediffusion.api.key}")
//    private String apiKey;
//
//    /**
//     * 使用给定的 GenerationRequest 生成图像。
//     *
//     * @param generationRequest 包含生成图像所需的参数
//     * @return 生成的图像的字节数组
//     * @throws IOException
//     * @throws ApiException 如果 API 调用失败
//     */
//    public byte[] generateImage(GenerationRequest generationRequest) throws IOException, ApiException {
//        // 创建 RestTemplate 实例
//        RestTemplate restTemplate = new RestTemplate();
//
//        // 设置错误处理，避免抛出异常
//        restTemplate.setErrorHandler(new ResponseErrorHandler() {
//            @Override
//            public boolean hasError(ClientHttpResponse response) throws IOException {
//                return false; // 不将任何响应视为错误
//            }
//
//            @Override
//            public void handleError(ClientHttpResponse response) throws IOException {
//                // 不执行任何操作
//            }
//        });
//
//        // 设置请求头
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(apiKey); // 使用 Bearer Token 进行授权
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        headers.setAccept(Collections.singletonList(MediaType.parseMediaType("image/*"))); // 接受图片类型
//
//        // 设置请求体
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("prompt", generationRequest.getPrompt()); // 添加提示词
//
//        // 添加可选参数
//        if (generationRequest.getMode() != null) {
//            body.add("mode", generationRequest.getMode());
//        }
//        if (generationRequest.getModel() != null) {
//            body.add("model", generationRequest.getModel());
//        }
//        if (generationRequest.getOutputFormat() != null) {
//            body.add("output_format", generationRequest.getOutputFormat());
//        }
//        if (generationRequest.getNegativePrompt() != null) {
//            body.add("negative_prompt", generationRequest.getNegativePrompt());
//        }
//        if (generationRequest.getAspectRatio() != null) {
//            body.add("aspect_ratio", generationRequest.getAspectRatio());
//        }
//        if (generationRequest.getSeed() != null) {
//            body.add("seed", generationRequest.getSeed());
//        }
//        if (generationRequest.getStrength() != null) {
//            body.add("strength", generationRequest.getStrength());
//        }
//
//        // 处理图像（仅当模式为 image-to-image 时）
//        if ("image-to-image".equals(generationRequest.getMode()) && generationRequest.getImage() != null) {
//            Resource imageResource = new ByteArrayResource(generationRequest.getImage()) {
//                @Override
//                public String getFilename() {
//                    return "image.png"; // 根据需要修改文件名
//                }
//            };
//            body.add("image", imageResource);
//        }
//
//        // 创建请求实体
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//        // 发送 POST 请求
//        ResponseEntity<byte[]> response = restTemplate.postForEntity(API_URL, requestEntity, byte[].class);
//
//        // 获取响应状态码
//        HttpStatus statusCode = (HttpStatus) response.getStatusCode();
//
//        if (statusCode == HttpStatus.OK) {
//            // 请求成功，返回图像字节数组
//            return response.getBody();
//        } else {
//            // 请求失败，抛出异常
//            throw new ApiException(statusCode);
//        }
//    }
//}


package com.storytime.service;

import com.storytime.model.GenerationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Collections;

/**
 * StableDiffusionService 类用于与 Stable Diffusion API 交互，生成图像。
 */
@Service
public class StableDiffusionService {

    private final RestTemplate restTemplate; // 使用构造函数注入的 RestTemplate

    // Stable Diffusion API 的 URL
    private static final String API_URL = "https://api.stability.ai/v2beta/stable-image/generate/sd3";

    @Value("${storytime.demo:false}")
    private boolean demoMode;

    @Value("${storytime.demo.image-resource:static/demo/demo.png}")
    private String demoImageResource;

    // 从配置文件中读取 API 密钥
    @Value("${stablediffusion.api.key}")
    private String apiKey;

    // 构造函数注入 RestTemplate
    public StableDiffusionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        configureRestTemplate(); // 配置 RestTemplate 错误处理
    }

    // 配置 RestTemplate 的错误处理
    private void configureRestTemplate() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false; // 不将任何响应视为错误
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // 不执行任何操作
            }
        });
    }

    /**
     * 使用给定的 GenerationRequest 生成图像。
     *
     * @param generationRequest 包含生成图像所需的参数
     * @return 生成的图像的字节数组
     * @throws IOException
     * @throws ApiException 如果 API 调用失败
     */
    public byte[] generateImage(GenerationRequest generationRequest) throws IOException, ApiException {
        if (demoMode) {
            ClassPathResource resource = new ClassPathResource(demoImageResource);
            return StreamUtils.copyToByteArray(resource.getInputStream());
        }

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey); // 使用 Bearer Token 进行授权
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.parseMediaType("image/*"))); // 接受图片类型

        // 设置请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("prompt", generationRequest.getPrompt()); // 添加提示词

        // 添加可选参数
        if (generationRequest.getMode() != null) {
            body.add("mode", generationRequest.getMode());
        }
        if (generationRequest.getModel() != null) {
            body.add("model", generationRequest.getModel());
        }
        if (generationRequest.getOutputFormat() != null) {
            body.add("output_format", generationRequest.getOutputFormat());
        }
        if (generationRequest.getNegativePrompt() != null) {
            body.add("negative_prompt", generationRequest.getNegativePrompt());
        }
        if (generationRequest.getAspectRatio() != null) {
            body.add("aspect_ratio", generationRequest.getAspectRatio());
        }
        if (generationRequest.getSeed() != null) {
            body.add("seed", generationRequest.getSeed());
        }
        if (generationRequest.getStrength() != null) {
            body.add("strength", generationRequest.getStrength());
        }

        // 处理图像（仅当模式为 image-to-image 时）
        if ("image-to-image".equals(generationRequest.getMode()) && generationRequest.getImage() != null) {
            Resource imageResource = new ByteArrayResource(generationRequest.getImage()) {
                @Override
                public String getFilename() {
                    return "image.png"; // 根据需要修改文件名
                }
            };
            body.add("image", imageResource);
        }

        // 创建请求实体
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 发送 POST 请求
        ResponseEntity<byte[]> response = restTemplate.postForEntity(API_URL, requestEntity, byte[].class);

        // 获取响应状态码
        HttpStatus statusCode = (HttpStatus) response.getStatusCode();

        if (statusCode == HttpStatus.OK) {
            // 请求成功，返回图像字节数组
            return response.getBody();
        } else {
            // 请求失败，抛出异常
            throw new ApiException(statusCode);
        }
    }
}
