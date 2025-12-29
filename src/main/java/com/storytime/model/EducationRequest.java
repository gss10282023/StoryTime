// EducationRequest.java
package com.storytime.model;

public class EducationRequest {
    private String mainCharacter;
    private String ageGroup;
    private String subject;
    private String userInput;
    private boolean generateVideo;

    // 构造函数
    public EducationRequest() {
    }

    public EducationRequest(String mainCharacter, String ageGroup, String subject, String userInput, boolean generateVideo) {
        this.mainCharacter = mainCharacter;
        this.ageGroup = ageGroup;
        this.subject = subject;
        this.userInput = userInput;
        this.generateVideo = generateVideo;
    }

    // Getter 和 Setter 方法

    public String getMainCharacter() {
        return mainCharacter;
    }

    public void setMainCharacter(String mainCharacter) {
        this.mainCharacter = mainCharacter;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public boolean isGenerateVideo() {
        return generateVideo;
    }

    public void setGenerateVideo(boolean generateVideo) {
        this.generateVideo = generateVideo;
    }
}
