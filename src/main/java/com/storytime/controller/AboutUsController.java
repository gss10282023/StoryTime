package com.storytime.controller;

import com.storytime.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutUsController {
    @GetMapping("/aboutUs")
    public String showLoginPage(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user); // 将用户信息传递给index.html
        }

        return "aboutUs";
    }
}
