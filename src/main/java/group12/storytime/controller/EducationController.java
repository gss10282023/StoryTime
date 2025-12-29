// EducationController.java
package group12.storytime.controller;

import group12.storytime.model.EducationRequest;
import group12.storytime.service.EducationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * EducationController 类用于处理教育内容生成的请求，包括生成教育句子、图片、音频和视频。
 */
@Controller
public class EducationController {

    @Autowired
    private EducationService educationService;

    // 显示教育视频生成的表单
    @GetMapping("/education")
    public String showEducationForm(Model model, HttpServletRequest httpServletRequest) {
        model.addAttribute("educationRequest", new EducationRequest());

        HttpSession session = httpServletRequest.getSession();
        String sessionId = session.getId();
        model.addAttribute("sessionId", "");
        return "educationForm";
    }

    // 处理表单提交，异步生成教育内容和对应的资源
    @PostMapping("/education")
    public String generateEducationContent(@ModelAttribute EducationRequest request, Model model, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        String sessionId = session.getId();  // 获取当前会话的 ID，用于标识 WebSocket 通道
        educationService.generateEducationContent(request, sessionId);  // 异步调用服务方法
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("educationRequest", request);
        System.out.println("EducationRequest: " + request);
        return "educationForm";  // 返回结果页面
    }
}
