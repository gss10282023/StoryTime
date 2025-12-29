package group12.storytime.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class StoryImageTest {

    private StoryImage storyImage;
    private UUID imageID;
    private UUID privateFairytaleID;
    private UUID publicFairytaleID;
    private String imageURL;

    @BeforeEach
    public void setUp() {
        storyImage = new StoryImage();
        imageID = UUID.randomUUID();
        privateFairytaleID = UUID.randomUUID();
        publicFairytaleID = UUID.randomUUID();
        imageURL = "https://example.com/image.png";
    }

    @Test
    public void testSetAndGetImageID() {
        storyImage.setImageID(imageID);
        assertEquals(imageID, storyImage.getImageID());
    }

    @Test
    public void testSetAndGetPrivateFairytaleID() {
        storyImage.setPrivateFairytaleID(privateFairytaleID);
        assertEquals(privateFairytaleID, storyImage.getPrivateFairytaleID());
    }

    @Test
    public void testSetAndGetPublicFairytaleID() {
        storyImage.setPublicFairytaleID(publicFairytaleID);
        assertEquals(publicFairytaleID, storyImage.getPublicFairytaleID());
    }

    @Test
    public void testSetAndGetImageURL() {
        storyImage.setImageURL(imageURL);
        assertEquals(imageURL, storyImage.getImageURL());
    }

    @Test
    public void testDefaultValues() {
        StoryImage defaultStoryImage = new StoryImage();
        assertNull(defaultStoryImage.getImageID());
        assertNull(defaultStoryImage.getPrivateFairytaleID());
        assertNull(defaultStoryImage.getPublicFairytaleID());
        assertNull(defaultStoryImage.getImageURL());
    }
}
