package com.storytime.service;

import com.storytime.entity.Video;
import com.storytime.repository.VideoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class VideoServiceTest {

    @Mock
    private VideoMapper videoMapper;

    @InjectMocks
    private VideoService videoService;

    @BeforeEach
    public void setUp() {
        // 初始化 Mockito 和 VideoService
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllVideos() {
        // 创建模拟视频列表
        List<Video> mockVideos = new ArrayList<>();
        Video video1 = new Video();
        video1.setVideoID(UUID.randomUUID());
        video1.setSubject("Math");
        mockVideos.add(video1);

        Video video2 = new Video();
        video2.setVideoID(UUID.randomUUID());
        video2.setSubject("Science");
        mockVideos.add(video2);

        // 模拟 videoMapper.findAllVideos() 返回的结果
        when(videoMapper.findAllVideos()).thenReturn(mockVideos);

        // 调用 VideoService 的 getAllVideos() 方法
        List<Video> videos = videoService.getAllVideos();

        // 验证结果
        assertEquals(2, videos.size());
        assertEquals("Math", videos.get(0).getSubject());
        assertEquals("Science", videos.get(1).getSubject());

        // 验证 videoMapper.findAllVideos() 方法是否被调用
        verify(videoMapper, times(1)).findAllVideos();
    }
}
