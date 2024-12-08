package com.example.laboratory6;

import jakarta.servlet.http.HttpSession;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/auth")
public class AuthThymeleafController {

    private final JavaMailSender mailSender;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthThymeleafController(AuthService authService, UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;

    }
    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "forgot-password"; // Имя HTML-шаблона в папке templates
    }
    @GetMapping("/reset-password")
    public String resetPasswordForm() {
        return "reset-password";
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


    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким email не найден"));

        String resetCode = String.valueOf((int) (Math.random() * 900000) + 100000); // Генерация 6-значного кода
        user.setResetCode(resetCode);
        userRepository.save(user);

        // Отправка письма
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Сброс пароля");
        message.setText("Ваш код для сброса пароля: " + resetCode);

        mailSender.send(message); // Отправка письма

        return "redirect:/web/auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String resetCode,
                                @RequestParam String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким email не найден"));

        if (!user.getResetCode().equals(resetCode)) {
            throw new IllegalArgumentException("Неверный код сброса");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetCode(null); // Очищаем код после успешного сброса
        userRepository.save(user);

        return ("redirect:/web/auth/login"); // Перенаправляем на страницу входа после успешного сброса
    }

}
