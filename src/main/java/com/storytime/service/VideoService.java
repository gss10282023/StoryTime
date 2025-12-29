package com.storytime.service;


import com.storytime.entity.Video;
import com.storytime.repository.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoMapper videoMapper;

    public List<Video> getAllVideos() {
        return videoMapper.findAllVideos();
    }
}
