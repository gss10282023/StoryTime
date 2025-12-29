package group12.storytime.controller;

import group12.storytime.model.GenerationRequest;
import group12.storytime.service.ApiException;
import group12.storytime.service.StableDiffusionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;

@RestController
public class StableDiffusionController {

    @Autowired
    private StableDiffusionService stableDiffusionService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/generate")
    public String generateImage(
            @RequestParam String prompt,
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) String model,
            @RequestParam(required = false, name = "output_format") String outputFormat,
            @RequestParam(required = false, name = "negative_prompt") String negativePrompt,
            @RequestParam(required = false) Integer seed,
            @RequestParam(required = false) Double strength,
            @RequestPart(required = false) MultipartFile image
    ) {
        // Build the GenerationRequest object
        GenerationRequest generationRequest = new GenerationRequest();
        generationRequest.setPrompt(prompt);

        if (StringUtils.hasText(mode)) {
            generationRequest.setMode(mode);
        }
        if (StringUtils.hasText(model)) {
            generationRequest.setModel(model);
        }
        if (StringUtils.hasText(outputFormat)) {
            generationRequest.setOutputFormat(outputFormat);
        }
        if (StringUtils.hasText(negativePrompt)) {
            generationRequest.setNegativePrompt(negativePrompt);
        }
        generationRequest.setSeed(seed);
        generationRequest.setStrength(strength);

        if (image != null && !image.isEmpty()) {
            try {
                generationRequest.setImage(image.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Asynchronously process image generation
        CompletableFuture.runAsync(() -> {
            try {
                byte[] imageBytes = stableDiffusionService.generateImage(generationRequest);



                // 直接将生成的图像转换为Base64字符串
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                // 通过WebSocket将Base64字符串发送给订阅的客户端
                messagingTemplate.convertAndSend("/topic/image", base64Image);

            } catch (ApiException e) {
                e.printStackTrace();
                String errorMessage = getErrorMessage(e.getStatusCode());
                // Send error message via WebSocket
                messagingTemplate.convertAndSend("/topic/error", errorMessage);
            } catch (Exception e) {
                e.printStackTrace();
                String errorMessage = "There is a problem with the connection. Please refresh the page.";
                messagingTemplate.convertAndSend("/topic/error", errorMessage);
            }
        });
        return "Request accepted, generating image...";
    }



    private String getErrorMessage(HttpStatus statusCode) {
        switch (statusCode) {
            case BAD_REQUEST: // 400
            case FORBIDDEN:   // 403
                return "Your input contains disallowed content. Please try again.";
            case REQUEST_ENTITY_TOO_LARGE: // 413
                return "Your request is larger than 10MiB.";
            case TOO_MANY_REQUESTS: // 429
                return "Too many requests. Please try again later.";
            default:
                return "There is a problem with the connection. Please refresh the page.";
        }
    }
}
