package group12.storytime.controller;


import group12.storytime.entity.Video;
import group12.storytime.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Video> videos = videoService.getAllVideos();
        model.addAttribute("videos", videos);
        return "dashboard"; // 返回的视图名称，与模板文件名对应
    }
}
