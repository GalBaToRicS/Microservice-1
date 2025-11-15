package ru.itmentor.spring.boot_security.demo.service;

import ru.itmentor.spring.boot_security.demo.model.dto.auth.JwtDto;
import ru.itmentor.spring.boot_security.demo.model.dto.auth.SignInDto;
import ru.itmentor.spring.boot_security.demo.model.dto.auth.SignUpDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuthService {

    JwtDto signUp(SignUpDto dto);

    JwtDto signIn(SignInDto dto);

    boolean check(String token) throws JsonProcessingException;
}