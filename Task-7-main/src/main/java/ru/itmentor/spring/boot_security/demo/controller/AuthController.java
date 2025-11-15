package ru.itmentor.spring.boot_security.demo.controller;

import ru.itmentor.spring.boot_security.demo.model.dto.auth.JwtDto;
import ru.itmentor.spring.boot_security.demo.model.dto.auth.SignInDto;
import ru.itmentor.spring.boot_security.demo.model.dto.auth.SignUpDto;
import ru.itmentor.spring.boot_security.demo.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ru.itmentor.spring.boot_security.demo.constant.Constant.AUTHORIZATION_HEADER;
import static ru.itmentor.spring.boot_security.demo.constant.Constant.BEARER_PREFIX;

/**
 * Контроллер для авторизации и аутентификации
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Регистрация нового пользователя
     *
     * @param dto объект SignUpDto с данными для регистрации
     * @return объект ResponseEntity с созданным пользователем типа UserDto и статусом OK
     */
    @PostMapping("/signUp")
    public ResponseEntity<JwtDto> signUp(@RequestBody SignUpDto dto) {
        return ResponseEntity.ok(authService.signUp(dto));
    }

    /**
     * Авторизация пользователя
     *
     * @param dto объект SignInDto с данными для авторизации
     * @return объект ResponseEntity с JWT-токеном типа JwtDto и статусом OK, если авторизация успешна
     */
    @PostMapping(value = "/signIn")
    public ResponseEntity<JwtDto> signIn(@RequestBody SignInDto dto) {
        return ResponseEntity.ok(authService.signIn(dto));
    }

    /**
     * Проверка валидности JWT-токена
     *
     * @param auth Bearer-токен
     * @return true, если JWT-токен действителен; false в противном случае
     * @throws JsonProcessingException если возникает ошибка при обработке токена
     */
    @PostMapping("/check")
    public ResponseEntity<Boolean> check(@RequestHeader(AUTHORIZATION_HEADER) String auth) throws JsonProcessingException {
        var token = auth.replace(BEARER_PREFIX, "");
        return ResponseEntity.ok(authService.check(token));
    }
}