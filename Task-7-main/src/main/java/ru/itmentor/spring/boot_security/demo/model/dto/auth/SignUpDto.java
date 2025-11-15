package ru.itmentor.spring.boot_security.demo.model.dto.auth;

import ru.itmentor.spring.boot_security.demo.model.dto.RoleDto;

import java.util.Set;

public record SignUpDto(
        String username,
        String password,
        String passwordConfirm,
        Set<RoleDto> roleList
) {
}