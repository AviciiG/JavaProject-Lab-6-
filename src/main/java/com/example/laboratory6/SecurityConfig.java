package com.example.laboratory6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Отключение CSRF для API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/web/auth/**").permitAll() // Доступ для Thymeleaf
                        .requestMatchers("/api/v1/auth/**").permitAll() // Доступ для API
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .httpBasic(httpBasic -> httpBasic.realmName("API")) // Basic Auth для API
                .formLogin(form -> form.disable()); // Отключение встроенной формы входа
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
