package com.example.laboratory6;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    public void registerUser(String username, String password, String name, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setEmail(email);
        userRepository.save(user);

        mailService.sendRegistrationConfirmation(email);
    }

    public boolean validateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    System.out.println("Validating user: " + username);
                    System.out.println("Encoded password from DB: " + user.getPassword());
                    boolean matches = passwordEncoder.matches(password, user.getPassword());
                    System.out.println("Password matches: " + matches);
                    return matches;
                })
                .orElseGet(() -> {
                    System.out.println("User not found: " + username);
                    return false;
                });
    }

}
