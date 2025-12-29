package group12.storytime.entity;

import java.util.UUID;

public class StoryImage {
    private UUID ImageID;
    private UUID PrivateFairytaleID;
    private UUID PublicFairytaleID; // This will be null
    private String ImageURL;

    // Getters and Setters
    public UUID getImageID() {
        return ImageID;
    }

    public void setImageID(UUID imageID) {
        ImageID = imageID;
    }

    public UUID getPrivateFairytaleID() {
        return PrivateFairytaleID;
    }

    public void setPrivateFairytaleID(UUID privateFairytaleID) {
        PrivateFairytaleID = privateFairytaleID;
    }

    public UUID getPublicFairytaleID() {
        return PublicFairytaleID;
    }

    public void setPublicFairytaleID(UUID publicFairytaleID) {
        PublicFairytaleID = publicFairytaleID;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
