package group12.storytime.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PrivateFairyTaleTest {

    private PrivateFairyTale privateFairyTale;
    private UUID privateFairyTaleID;
    private UUID userID;
    private String title;
    private String content;
    private String language;
    private String audioURL;

    @BeforeEach
    public void setUp() {
        privateFairyTale = new PrivateFairyTale();
        privateFairyTaleID = UUID.randomUUID();
        userID = UUID.randomUUID();
        title = "The Brave Little Dragon";
        content = "Once upon a time, there was a brave little dragon...";
        language = "English";
        audioURL = "https://example.com/audio.mp3";
    }

    @Test
    public void testSetAndGetPrivateFairyTaleID() {
        privateFairyTale.setPrivateFairytaleID(privateFairyTaleID);
        assertEquals(privateFairyTaleID, privateFairyTale.getPrivateFairyTaleID());
    }

    @Test
    public void testSetAndGetUserID() {
        privateFairyTale.setUserID(userID);
        assertEquals(userID, privateFairyTale.getUserID());
    }

    @Test
    public void testSetAndGetTitle() {
        privateFairyTale.setTitle(title);
        assertEquals(title, privateFairyTale.getTitle());
    }

    @Test
    public void testSetAndGetContent() {
        privateFairyTale.setContent(content);
        assertEquals(content, privateFairyTale.getContent());
    }

    @Test
    public void testSetAndGetLanguage() {
        privateFairyTale.setLanguage(language);
        assertEquals(language, privateFairyTale.getLanguage());
    }

    @Test
    public void testSetAndGetAudioURL() {
        privateFairyTale.setAudioURL(audioURL);
        assertEquals(audioURL, privateFairyTale.getAudioURL());
    }

    @Test
    public void testDefaultValues() {
        PrivateFairyTale defaultPrivateFairyTale = new PrivateFairyTale();
        assertNull(defaultPrivateFairyTale.getPrivateFairyTaleID());
        assertNull(defaultPrivateFairyTale.getUserID());
        assertNull(defaultPrivateFairyTale.getTitle());
        assertNull(defaultPrivateFairyTale.getContent());
        assertNull(defaultPrivateFairyTale.getLanguage());
        assertNull(defaultPrivateFairyTale.getAudioURL());
    }
}
