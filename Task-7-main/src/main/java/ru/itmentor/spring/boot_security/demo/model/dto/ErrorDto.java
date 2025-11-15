package ru.itmentor.spring.boot_security.demo.model.dto;

public record ErrorDto(
        String errorMessage,
        Integer errorCode) {
}