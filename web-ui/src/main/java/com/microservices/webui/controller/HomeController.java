package com.microservices.webui.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador principal
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isAuthenticated", true);
        } else {
            model.addAttribute("isAuthenticated", false);
        }
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("username", authentication.getName());
        model.addAttribute("authorities", authentication.getAuthorities());
        return "dashboard";
    }

    @GetMapping("/users")
    public String users(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("username", authentication.getName());
        return "users";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("username", authentication.getName());
        return "profile";
    }
}
