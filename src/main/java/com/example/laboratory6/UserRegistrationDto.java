package com.example.laboratory6;

public class UserRegistrationDto {
    private String name;
    private String username;
    private String password;
    private String email; // Поле email есть

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() { // Геттер для email
        return email;
    }

    public void setEmail(String email) { // Сеттер для email
        this.email = email;
    }
}
