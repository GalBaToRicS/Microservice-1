package ru.itmentor.spring.boot_security.demo.service;

import ru.itmentor.spring.boot_security.demo.model.dto.UserDto;
import ru.itmentor.spring.boot_security.demo.model.dto.create.UserCreateDto;
import ru.itmentor.spring.boot_security.demo.model.dto.update.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserDto findCurrentUser(String auth);

    UserDto findById(Long id);

    List<UserDto> findAll();

    UserDto create(UserCreateDto dto);

    UserDto update(Long id, UserUpdateDto dto);

    void deleteById(Long id);

    void logout(String token);
}