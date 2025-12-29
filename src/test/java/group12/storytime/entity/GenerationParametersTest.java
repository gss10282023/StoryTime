package group12.storytime.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GenerationParametersTest {

    private GenerationParameters generationParameters;
    private UUID parameterID;
    private UUID videoID;
    private String subject;
    private String ageGroup;
    private String mainCharacter;
    private String language;
    private UUID exerciseID;

    @BeforeEach
    public void setUp() {
        generationParameters = new GenerationParameters();
        parameterID = UUID.randomUUID();
        videoID = UUID.randomUUID();
        subject = "Math";
        ageGroup = "6-8";
        mainCharacter = "Rabbit";
        language = "English";
        exerciseID = UUID.randomUUID();
    }

    @Test
    public void testSetAndGetParameterID() {
        generationParameters.setParameterID(parameterID);
        assertEquals(parameterID, generationParameters.getParameterID());
    }

    @Test
    public void testSetAndGetVideoID() {
        generationParameters.setVideoID(videoID);
        assertEquals(videoID, generationParameters.getVideoID());
    }

    @Test
    public void testSetAndGetSubject() {
        generationParameters.setSubject(subject);
        assertEquals(subject, generationParameters.getSubject());
    }

    @Test
    public void testSetAndGetAgeGroup() {
        generationParameters.setAgeGroup(ageGroup);
        assertEquals(ageGroup, generationParameters.getAgeGroup());
    }

    @Test
    public void testSetAndGetMainCharacter() {
        generationParameters.setMainCharacter(mainCharacter);
        assertEquals(mainCharacter, generationParameters.getMainCharacter());
    }

    @Test
    public void testSetAndGetLanguage() {
        generationParameters.setLanguage(language);
        assertEquals(language, generationParameters.getLanguage());
    }

    @Test
    public void testSetAndGetExerciseID() {
        generationParameters.setExerciseID(exerciseID);
        assertEquals(exerciseID, generationParameters.getExerciseID());
    }

    @Test
    public void testSetAndGetNullableFields() {
        generationParameters.setMainCharacter(null);
        assertNull(generationParameters.getMainCharacter());

        generationParameters.setExerciseID(null);
        assertNull(generationParameters.getExerciseID());
    }

    @Test
    public void testDefaultValues() {
        GenerationParameters defaultParameters = new GenerationParameters();
        assertNull(defaultParameters.getParameterID());
        assertNull(defaultParameters.getVideoID());
        assertNull(defaultParameters.getSubject());
        assertNull(defaultParameters.getAgeGroup());
        assertNull(defaultParameters.getMainCharacter());
        assertNull(defaultParameters.getLanguage());
        assertNull(defaultParameters.getExerciseID());
    }
}
