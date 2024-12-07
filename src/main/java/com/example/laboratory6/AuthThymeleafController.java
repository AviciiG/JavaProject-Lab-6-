package com.example.laboratory6;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/auth")
public class AuthThymeleafController {

    private final AuthService authService;

    public AuthThymeleafController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register"; // Убедитесь, что register.html существует
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String name,
                               @RequestParam String email,
                               Model model) {
        try {
            authService.registerUser(username, password, name, email);
            return "redirect:/web/auth/login"; // После успешной регистрации перенаправляем на логин
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "register"; // Возврат на форму регистрации с сообщением об ошибке
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        if (authService.validateUser(username, password)) {
            session.setAttribute("username", username); // Сохраняем пользователя в сессии
            return "redirect:/web/tasks"; // Перенаправляем на задачи
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login"; // Возврат на страницу входа
        }
    }



    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Завершаем сессию
        return "redirect:/web/auth/login"; // Перенаправляем на логин
    }
}
