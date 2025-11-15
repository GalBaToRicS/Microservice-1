package ru.itmentor.spring.boot_security.demo.constant;

import lombok.Getter;

/**
 * Перечисление, представляющее роли пользователей в системе.
 * <p>
 * Это перечисление содержит доступные роли, используемые для управления
 * правами доступа к ресурсам приложения.
 */
@Getter
public enum RoleName {

    ADMIN,
    USER
}