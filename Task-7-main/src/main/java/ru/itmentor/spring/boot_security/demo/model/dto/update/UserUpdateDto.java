package ru.itmentor.spring.boot_security.demo.model.dto.update;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {

    private Long id;
    private String username;
}