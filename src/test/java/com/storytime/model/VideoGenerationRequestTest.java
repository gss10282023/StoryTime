package com.storytime.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VideoGenerationRequestTest {

    private VideoGenerationRequest videoGenerationRequest;

    @BeforeEach
    public void setUp() {
        videoGenerationRequest = new VideoGenerationRequest();
    }

    @Test
    public void testPrompt() {
        String prompt = "Create a video of a beautiful forest.";
        videoGenerationRequest.setPrompt(prompt);
        assertEquals(prompt, videoGenerationRequest.getPrompt());
    }

    @Test
    public void testImageUrl() {
        String imageUrl = "https://example.com/image.jpg";
        videoGenerationRequest.setImageUrl(imageUrl);
        assertEquals(imageUrl, videoGenerationRequest.getImageUrl());
    }
}
