package com.storytime.entity;

import java.util.UUID;

public class GenerationParameters {
    private UUID ParameterID;
    private UUID VideoID;
    private String Subject;
    private String AgeGroup;
    private String MainCharacter; // 可为空
    private String Language;
    private UUID ExerciseID; // 可为空

    // Getters and Setters
    public UUID getParameterID() {
        return ParameterID;
    }

    public void setParameterID(UUID parameterID) {
        ParameterID = parameterID;
    }

    public UUID getVideoID() {
        return VideoID;
    }

    public void setVideoID(UUID videoID) {
        VideoID = videoID;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getAgeGroup() {
        return AgeGroup;
    }

    public void setAgeGroup(String ageGroup) {
        AgeGroup = ageGroup;
    }

    public String getMainCharacter() {
        return MainCharacter;
    }

    public void setMainCharacter(String mainCharacter) {
        MainCharacter = mainCharacter;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public UUID getExerciseID() {
        return ExerciseID;
    }

    public void setExerciseID(UUID exerciseID) {
        ExerciseID = exerciseID;
    }
}
