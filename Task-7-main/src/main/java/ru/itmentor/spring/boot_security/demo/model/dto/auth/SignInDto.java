package ru.itmentor.spring.boot_security.demo.model.dto.auth;

public record SignInDto(
        String username,
        String password) {
}