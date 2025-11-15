package ru.itmentor.spring.boot_security.demo.model.dto;

import ru.itmentor.spring.boot_security.demo.constant.RoleName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {

    private RoleName name;
}