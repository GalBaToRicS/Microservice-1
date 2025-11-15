package ru.itmentor.spring.boot_security.demo.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {

    private String id;
    private String login;
    private Set<RoleDto> roleList;
}