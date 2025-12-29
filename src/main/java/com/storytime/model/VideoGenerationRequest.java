package com.storytime.model;

public class VideoGenerationRequest {
    private String prompt;
    private String imageUrl; // 新增字段

    // Getters and Setters

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
