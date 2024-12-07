package com.example.laboratory6;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Отправка простого письма.
     *
     * @param to      Email получателя.
     * @param subject Тема письма.
     * @param text    Текст письма.
     */
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("your-email@example.com"); // Укажите email отправителя

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка отправки письма: " + e.getMessage());
        }
    }

    /**
     * Отправка подтверждения регистрации.
     *
     * @param userEmail Email пользователя.
     */
    public void sendRegistrationConfirmation(String userEmail) {
        String subject = "Подтверждение регистрации";
        String text = "Добро пожаловать! Ваша регистрация прошла успешно.";
        sendSimpleMessage(userEmail, subject, text);
    }
}
