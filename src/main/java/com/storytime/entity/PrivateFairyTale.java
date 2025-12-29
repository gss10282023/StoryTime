package com.storytime.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class PrivateFairyTale {
    private UUID PrivateFairyTaleID;
    private UUID UserID;
    private String Title;
    private String Content;
    private String Language;
    private String AudioURL;

    // Getters and Setters
    public UUID getPrivateFairyTaleID() {
        return PrivateFairyTaleID;
    }

    public void setPrivateFairytaleID(UUID privateFairytaleID) {
        PrivateFairyTaleID = privateFairytaleID;
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getAudioURL() {
        return AudioURL;
    }

    public void setAudioURL(String audioURL) {
        AudioURL = audioURL;
    }

    // 其他属性的 Getters 和 Setters，同样调整命名
}
