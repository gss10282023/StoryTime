package group12.storytime.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import group12.storytime.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;


@Controller
public class HomeController {

    @GetMapping("/")
    public String showLoginPage(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user); // 将用户信息传递给index.html
        }
        return "index";
    }
}
