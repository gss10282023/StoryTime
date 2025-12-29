package group12.storytime.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VideoGenerationResponseTest {

    private VideoGenerationResponse videoGenerationResponse;

    @BeforeEach
    public void setUp() {
        videoGenerationResponse = new VideoGenerationResponse();
    }

    @Test
    public void testId() {
        String id = "12345";
        videoGenerationResponse.setId(id);
        assertEquals(id, videoGenerationResponse.getId());
    }

    @Test
    public void testState() {
        String state = "succeeded";
        videoGenerationResponse.setState(state);
        assertEquals(state, videoGenerationResponse.getState());
    }

    @Test
    public void testFailureReason() {
        String failureReason = "Timeout";
        videoGenerationResponse.setFailureReason(failureReason);
        assertEquals(failureReason, videoGenerationResponse.getFailureReason());
    }

    @Test
    public void testCreatedAt() {
        String createdAt = "2024-10-23T12:34:56";
        videoGenerationResponse.setCreatedAt(createdAt);
        assertEquals(createdAt, videoGenerationResponse.getCreatedAt());
    }

    @Test
    public void testAssets() {
        VideoGenerationResponse.Assets assets = new VideoGenerationResponse.Assets();
        String video = "https://example.com/video.mp4";
        assets.setVideo(video);

        videoGenerationResponse.setAssets(assets);
        assertNotNull(videoGenerationResponse.getAssets());
        assertEquals(video, videoGenerationResponse.getAssets().getVideo());
    }

    @Test
    public void testVersion() {
        String version = "1.0";
        videoGenerationResponse.setVersion(version);
        assertEquals(version, videoGenerationResponse.getVersion());
    }

    @Test
    public void testRequest() {
        VideoGenerationRequest request = new VideoGenerationRequest();
        request.setPrompt("Generate a video of a beach.");

        videoGenerationResponse.setRequest(request);
        assertNotNull(videoGenerationResponse.getRequest());
        assertEquals("Generate a video of a beach.", videoGenerationResponse.getRequest().getPrompt());
    }

    @Test
    public void testGetResultUrl_Succeeded() {
        // 设置状态为 "succeeded" 并设置有效的 assets
        videoGenerationResponse.setState("succeeded");

        VideoGenerationResponse.Assets assets = new VideoGenerationResponse.Assets();
        String videoUrl = "https://example.com/video.mp4";
        assets.setVideo(videoUrl);
        videoGenerationResponse.setAssets(assets);

        // 验证结果 URL
        assertEquals(videoUrl, videoGenerationResponse.getResultUrl());
    }

    @Test
    public void testGetResultUrl_NotSucceeded() {
        // 设置状态为非 "succeeded"
        videoGenerationResponse.setState("failed");

        VideoGenerationResponse.Assets assets = new VideoGenerationResponse.Assets();
        assets.setVideo("https://example.com/video.mp4");
        videoGenerationResponse.setAssets(assets);

        // 验证结果 URL 应为 null
        assertNull(videoGenerationResponse.getResultUrl());
    }

    @Test
    public void testGetResultUrl_NullAssets() {
        // 设置状态为 "succeeded" 但 assets 为 null
        videoGenerationResponse.setState("succeeded");
        videoGenerationResponse.setAssets(null);

        // 验证结果 URL 应为 null
        assertNull(videoGenerationResponse.getResultUrl());
    }

}
