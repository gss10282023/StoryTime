package com.storytime.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenerationRequestTest {

    private GenerationRequest generationRequest;

    @BeforeEach
    public void setUp() {
        generationRequest = new GenerationRequest();
    }

    @Test
    public void testPrompt() {
        String prompt = "Generate an image of a sunset";
        generationRequest.setPrompt(prompt);
        assertEquals(prompt, generationRequest.getPrompt());
    }

    @Test
    public void testMode() {
        String mode = "artistic";
        generationRequest.setMode(mode);
        assertEquals(mode, generationRequest.getMode());
    }

    @Test
    public void testModel() {
        String model = "GPT-4";
        generationRequest.setModel(model);
        assertEquals(model, generationRequest.getModel());
    }

    @Test
    public void testOutputFormat() {
        String outputFormat = "JPEG";
        generationRequest.setOutputFormat(outputFormat);
        assertEquals(outputFormat, generationRequest.getOutputFormat());
    }

    @Test
    public void testNegativePrompt() {
        String negativePrompt = "Avoid dark colors";
        generationRequest.setNegativePrompt(negativePrompt);
        assertEquals(negativePrompt, generationRequest.getNegativePrompt());
    }

    @Test
    public void testAspectRatio() {
        String aspectRatio = "16:9";
        generationRequest.setAspectRatio(aspectRatio);
        assertEquals(aspectRatio, generationRequest.getAspectRatio());
    }

    @Test
    public void testSeed() {
        Integer seed = 42;
        generationRequest.setSeed(seed);
        assertEquals(seed, generationRequest.getSeed());
    }

    @Test
    public void testStrength() {
        Double strength = 0.8;
        generationRequest.setStrength(strength);
        assertEquals(strength, generationRequest.getStrength());
    }

    @Test
    public void testImage() {
        byte[] image = new byte[]{1, 2, 3, 4, 5};
        generationRequest.setImage(image);
        assertArrayEquals(image, generationRequest.getImage());
    }
}
