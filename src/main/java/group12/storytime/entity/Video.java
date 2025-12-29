package group12.storytime.entity;

import java.sql.Timestamp;
import java.util.UUID;

public class Video {
    private UUID VideoID;
    private UUID UserID;
    private Timestamp CreationDate;
    private String Subject;
    private Integer AgeGroup;
    private String MainCharacter; // 可为空
    private String VideoURL;
    private String Language; // 可为空

    // Getters and Setters
    public UUID getVideoID() {
        return VideoID;
    }

    public void setVideoID(UUID videoID) {
        VideoID = videoID;
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public Timestamp getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        CreationDate = creationDate;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public Integer getAgeGroup() {
        return AgeGroup;
    }

    public void setAgeGroup(Integer ageGroup) {
        AgeGroup = ageGroup;
    }

    public String getMainCharacter() {
        return MainCharacter;
    }

    public void setMainCharacter(String mainCharacter) {
        MainCharacter = mainCharacter;
    }

    public String getVideoURL() {
        return VideoURL;
    }

    public void setVideoURL(String videoURL) {
        VideoURL = videoURL;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }
}
