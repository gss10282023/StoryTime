package group12.storytime.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FairytaleContentTest {

    private FairytaleContent fairytaleContent;

    @BeforeEach
    public void setUp() {
        fairytaleContent = new FairytaleContent();
    }

    @Test
    public void testTitle() {
        String title = "The Brave Little Dragon";
        fairytaleContent.setTitle(title);
        assertEquals(title, fairytaleContent.getTitle());
    }

    @Test
    public void testAgeGroup() {
        String ageGroup = "5-7";
        fairytaleContent.setAgeGroup(ageGroup);
        assertEquals(ageGroup, fairytaleContent.getAgeGroup());
    }

    @Test
    public void testGender() {
        String gender = "Female";
        fairytaleContent.setGender(gender);
        assertEquals(gender, fairytaleContent.getGender());
    }

    @Test
    public void testCharacteristics() {
        String characteristics = "Brave, kind, curious";
        fairytaleContent.setCharacteristics(characteristics);
        assertEquals(characteristics, fairytaleContent.getCharacteristics());
    }

    @Test
    public void testFairyTaleText() {
        String fairyTaleText = "Once upon a time, there was a brave little dragon...";
        fairytaleContent.setFairyTaleText(fairyTaleText);
        assertEquals(fairyTaleText, fairytaleContent.getFairyTaleText());
    }

    @Test
    public void testSettersAndGetters() {
        // 测试所有字段的 setter 和 getter
        String title = "The Brave Little Dragon";
        String ageGroup = "5-7";
        String gender = "Female";
        String characteristics = "Brave, kind, curious";
        String fairyTaleText = "Once upon a time, there was a brave little dragon...";

        fairytaleContent.setTitle(title);
        fairytaleContent.setAgeGroup(ageGroup);
        fairytaleContent.setGender(gender);
        fairytaleContent.setCharacteristics(characteristics);
        fairytaleContent.setFairyTaleText(fairyTaleText);

        assertEquals(title, fairytaleContent.getTitle());
        assertEquals(ageGroup, fairytaleContent.getAgeGroup());
        assertEquals(gender, fairytaleContent.getGender());
        assertEquals(characteristics, fairytaleContent.getCharacteristics());
        assertEquals(fairyTaleText, fairytaleContent.getFairyTaleText());
    }
}
