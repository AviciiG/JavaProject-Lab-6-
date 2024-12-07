package com.example.laboratory6;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model; // Импортируем правильный класс
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;

    public AuthController(AuthService authService, MailService mailService) {
        this.authService = authService;
        this.mailService = mailService;
    }

    // Регистрация пользователя
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto registrationDto) {
        try {
            // Регистрация пользователя
            authService.registerUser(
                    registrationDto.getUsername(),
                    registrationDto.getPassword(),
                    registrationDto.getName(),
                    registrationDto.getEmail() // Добавлен email
            );

            // Отправляем уведомление о регистрации
            mailService.sendRegistrationConfirmation(registrationDto.getEmail());

            return ResponseEntity.ok("Пользователь успешно зарегистрирован");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Ошибка регистрации: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка сервера: " + e.getMessage());
        }
    }

    // Логин пользователя
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) { // Используем правильный Model
        if (authService.validateUser(username, password)) {
            session.setAttribute("username", username); // Сохраняем пользователя в сессии
            return "redirect:/web/tasks"; // Перенаправляем на задачи
        } else {
            model.addAttribute("error", "Invalid username or password"); // Добавляем сообщение об ошибке
            return "login"; // Возврат на страницу входа с ошибкой
        }
    }

}
