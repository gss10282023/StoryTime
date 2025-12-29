package group12.storytime.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class VideoTest {

    private Video video;
    private UUID videoID;
    private UUID userID;
    private Timestamp creationDate;
    private String subject;
    private Integer ageGroup;
    private String mainCharacter;
    private String videoURL;
    private String language;

    @BeforeEach
    public void setUp() {
        video = new Video();
        videoID = UUID.randomUUID();
        userID = UUID.randomUUID();
        creationDate = new Timestamp(System.currentTimeMillis());
        subject = "Math";
        ageGroup = 5;
        mainCharacter = "Curious Cat";
        videoURL = "https://example.com/video.mp4";
        language = "English";
    }

    @Test
    public void testSetAndGetVideoID() {
        video.setVideoID(videoID);
        assertEquals(videoID, video.getVideoID());
    }

    @Test
    public void testSetAndGetUserID() {
        video.setUserID(userID);
        assertEquals(userID, video.getUserID());
    }

    @Test
    public void testSetAndGetCreationDate() {
        video.setCreationDate(creationDate);
        assertEquals(creationDate, video.getCreationDate());
    }

    @Test
    public void testSetAndGetSubject() {
        video.setSubject(subject);
        assertEquals(subject, video.getSubject());
    }

    @Test
    public void testSetAndGetAgeGroup() {
        video.setAgeGroup(ageGroup);
        assertEquals(ageGroup, video.getAgeGroup());
    }

    @Test
    public void testSetAndGetMainCharacter() {
        video.setMainCharacter(mainCharacter);
        assertEquals(mainCharacter, video.getMainCharacter());
    }

    @Test
    public void testSetAndGetVideoURL() {
        video.setVideoURL(videoURL);
        assertEquals(videoURL, video.getVideoURL());
    }

    @Test
    public void testSetAndGetLanguage() {
        video.setLanguage(language);
        assertEquals(language, video.getLanguage());
    }

    @Test
    public void testSetAndGetNullableFields() {
        video.setMainCharacter(null);
        assertNull(video.getMainCharacter());

        video.setLanguage(null);
        assertNull(video.getLanguage());
    }
}
