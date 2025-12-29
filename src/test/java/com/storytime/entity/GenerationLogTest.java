package com.storytime.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GenerationLogTest {

    private GenerationLog generationLog;
    private UUID logID;
    private UUID userID;
    private UUID videoID;
    private Timestamp timestamp;

    @BeforeEach
    public void setUp() {
        generationLog = new GenerationLog();
        logID = UUID.randomUUID();
        userID = UUID.randomUUID();
        videoID = UUID.randomUUID();
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    @Test
    public void testSetAndGetLogID() {
        generationLog.setLogID(logID);
        assertEquals(logID, generationLog.getLogID());
    }

    @Test
    public void testSetAndGetUserID() {
        generationLog.setUserID(userID);
        assertEquals(userID, generationLog.getUserID());
    }

    @Test
    public void testSetAndGetVideoID() {
        generationLog.setVideoID(videoID);
        assertEquals(videoID, generationLog.getVideoID());
    }

    @Test
    public void testSetAndGetTimestamp() {
        generationLog.setTimestamp(timestamp);
        assertEquals(timestamp, generationLog.getTimestamp());
    }

    @Test
    public void testDefaultValues() {
        GenerationLog defaultLog = new GenerationLog();
        assertNull(defaultLog.getLogID());
        assertNull(defaultLog.getUserID());
        assertNull(defaultLog.getVideoID());
        assertNull(defaultLog.getTimestamp());
    }
}
