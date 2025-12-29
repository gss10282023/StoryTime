package com.storytime.controller;

import com.storytime.entity.User;
import com.storytime.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }


    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);  // 将登录用户信息存储到Session中
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           RedirectAttributes redirectAttributes) {
        // 创建新的 User 对象，并设置必要的字段
        User user = new User(username, password, email);

        try {
            // 调用 UserService 的 register 方法，执行用户注册逻辑
            userService.register(user);
            // 注册成功，将成功消息放入 redirectAttributes 中
            redirectAttributes.addFlashAttribute("message", "Registration successful. Please login.");
            redirectAttributes.addFlashAttribute("signup", true); // 设置signup状态为true
            log.info("RedirectAttributes contains message: {}", redirectAttributes.getFlashAttributes().get("message"));
            return "redirect:/login";  // 重定向到登录页面
        } catch (Exception e) {
            // register fail, put message in redirectAttributes
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("signup", true); // 设置signup状态为true
            return "redirect:/login";
        }
    }



    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String newPassword,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.resetPassword(username, email, newPassword);
            redirectAttributes.addFlashAttribute("message", "Password reset successful.");
            return "redirect:/user/resetPassword"; // 重定向到resetPassword页面，展示成功消息
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/resetPassword"; // 重定向回页面并展示错误信息
        }
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordPage() {
        return "resetPassword"; // 对应resetPassword.html
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            return "profile";
        } else {
            return "redirect:/login";
        }
    }

    //Show the change username page
    @GetMapping("/changeUsername")
    public String showChangeUsernamePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);  // 可选：将当前用户信息传递到页面
            return "changeUsername";  // 返回 changeUsername.html 页面
        } else {
            return "redirect:/user/profile";
        }
    }

    //Show the change email page
    @GetMapping("/changeEmail")
    public String showChangeEmailPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);  // pass the current user information to the page
            return "changeEmail";  // return the changeEmail.html page
        } else {
            return "redirect:/user/profile";
        }
    }

    @PostMapping("/changeUsername")
    public String changeUsername(@RequestParam String newUsername,
                                 @RequestParam String password,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                userService.changeUsername(user.getUsername(), newUsername, password);
                user.setUsername(newUsername); // update the username in the session
                session.setAttribute("user", user);
                redirectAttributes.addFlashAttribute("message", "Username changed successfully");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
            return "redirect:/user/profile";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/changeEmail")
    public String changeEmail(@RequestParam String newEmail,
                              @RequestParam String password,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                userService.changeEmail(user.getUsername(), newEmail, password);
                user.setEmail(newEmail);
                session.setAttribute("user", user);
                redirectAttributes.addFlashAttribute("message", "Email changed successfully");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
            return "redirect:/user/profile";
        } else {
            return "redirect:/login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();  // Disable the current session
        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully.");
        return "redirect:/";  // Redirect to the homepage
    }

}
