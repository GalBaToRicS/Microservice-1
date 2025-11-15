package ru.itmentor.spring.boot_security.demo.model.entity;

import ru.itmentor.spring.boot_security.demo.constant.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Класс, представляющий пакет данных, содержащих полезную информацию о токене.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenPayload {

    private String username;
    private Set<RoleName> roles;

    /**
     * Время валидности токена
     */
    private Long exp;
}