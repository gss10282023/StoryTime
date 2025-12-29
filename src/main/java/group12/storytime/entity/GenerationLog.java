package group12.storytime.entity;

import java.sql.Timestamp;
import java.util.UUID;

public class GenerationLog {
    private UUID LogID;
    private UUID UserID;
    private UUID VideoID;
    private Timestamp Timestamp;

    // Getters and Setters
    public UUID getLogID() {
        return LogID;
    }

    public void setLogID(UUID logID) {
        LogID = logID;
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public UUID getVideoID() {
        return VideoID;
    }

    public void setVideoID(UUID videoID) {
        VideoID = videoID;
    }

    public Timestamp getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        Timestamp = timestamp;
    }
}
