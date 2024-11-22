package com.example.laboratory6;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web/auth")
public class AuthThymeleafController {

    private final AuthService authService;

    public AuthThymeleafController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String name,
                               Model model) {
        try {
            authService.registerUser(username, password, name);
            return "redirect:/web/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        if (authService.validateUser(username, password)) {
            return "redirect:/web/tasks";
        } else {
            model.addAttribute("error", "Неверное имя пользователя или пароль");
            return "login";
        }
    }
}
