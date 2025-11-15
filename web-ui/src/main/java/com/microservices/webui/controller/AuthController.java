package com.microservices.webui.controller;

import com.microservices.webui.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la interfaz de autenticación
 */
@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDto request, Model model) {
        log.info("Registrando nuevo usuario: {}", request.getUsername());
        try {
            authService.register(request);
            model.addAttribute("success", "Usuario registrado exitosamente");
            return "redirect:/auth/login";
        } catch (Exception e) {
            log.error("Error en registro", e);
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    public static class RegisterDto {
        public String username;
        public String email;
        public String password;
        public String confirmPassword;
        public String firstName;
        public String lastName;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
    }
}
