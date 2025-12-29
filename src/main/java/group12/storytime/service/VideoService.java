package group12.storytime.service;


import group12.storytime.entity.Video;
import group12.storytime.repository.VideoMapper;
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
