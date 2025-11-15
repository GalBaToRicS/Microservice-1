package ru.itmentor.spring.boot_security.demo.model.dto.create;

import ru.itmentor.spring.boot_security.demo.model.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserCreateDto {

    private String username;
    private String password;
    private String passwordConfirm;
    private Set<Role> roleList;
}