package com.storytime.controller;

import com.storytime.model.FairytaleRequest;
import com.storytime.service.FairytaleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 * FairytaleIntegrationController 类用于处理童话故事生成的请求。
 */
@Controller
public class FairytaleIntegrationController {

    @Autowired
    private FairytaleService fairytaleService;

    @Value("${storytime.demo:false}")
    private boolean demoMode;

    // 显示用户输入页面
    @GetMapping("/fairytale")
    public String showFairytaleForm(Model model, HttpServletRequest httpServletRequest) {
        FairytaleRequest fairytaleRequest = new FairytaleRequest();
        if (demoMode) {
            fairytaleRequest.setGenerateVideo(true);
        }
        model.addAttribute("fairytaleRequest", fairytaleRequest);
        HttpSession session = httpServletRequest.getSession();
        String sessionId = session.getId();
        model.addAttribute("sessionId", "");
        return "fairytaleForm";
    }

    // 处理表单提交，异步生成故事和对应的资源
    @PostMapping("/fairytale")
    public String generateFairytale(@ModelAttribute FairytaleRequest request, Model model, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        String sessionId = session.getId();
        fairytaleService.generateFairytale(request, sessionId);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("fairytaleRequest", request);
        model.addAttribute("generateVideo", true); // 添加这行
        System.out.println("FairytaleRequest: " + request);
        return "fairytaleForm";
    }
}
