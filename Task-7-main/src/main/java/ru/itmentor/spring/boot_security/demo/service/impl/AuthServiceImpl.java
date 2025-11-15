package ru.itmentor.spring.boot_security.demo.service.impl;

import ru.itmentor.spring.boot_security.demo.config.TokenProvider;
import ru.itmentor.spring.boot_security.demo.exception.EntityNotFoundException;
import ru.itmentor.spring.boot_security.demo.exception.InvalidJwtException;
import ru.itmentor.spring.boot_security.demo.model.dto.auth.JwtDto;
import ru.itmentor.spring.boot_security.demo.model.dto.auth.SignInDto;
import ru.itmentor.spring.boot_security.demo.model.dto.auth.SignUpDto;
import ru.itmentor.spring.boot_security.demo.model.entity.User;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repository.TokenRepository;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;
import ru.itmentor.spring.boot_security.demo.service.AuthService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

import static ru.itmentor.spring.boot_security.demo.constant.Constant.*;

/**
 * Реализация сервиса аутентификации
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    /**
     * Метод для регистрации нового пользователя
     *
     * @param dto объект SignUpDto, содержащий данные для регистрации
     * @return объект JwtDto, содержащий JWT-токен
     */
    @Override
    @Transactional
    public JwtDto signUp(SignUpDto dto) {
        if (!dto.password().equals(dto.passwordConfirm())) {
            throw new InvalidJwtException(PASSWORD_ERROR);
        }
        if (Boolean.TRUE.equals(userRepository.existsUserByLogin(dto.username()))) {
            throw new InvalidJwtException(USERNAME_IS_EXIST);
        }
        var encryptedPassword = passwordEncoder.encode(dto.password());
        var roleSet = dto.roleList().stream()
                .map(roleDto -> roleRepository.findByName(roleDto.getName()).orElseThrow(EntityNotFoundException::new))
                .collect(Collectors.toSet());
        var newUser = new User(dto.username(), encryptedPassword, roleSet);
        userRepository.save(newUser);
        return buildJwt(dto.username(), dto.password());
    }

    /**
     * Метод для аутентификации пользователя
     *
     * @param dto объект SignInDto, содержащий данные для аутентификации
     * @return объект JwtDto, содержащий JWT-токен
     */
    @Override
    @Transactional
    public JwtDto signIn(SignInDto dto) {
        try {
            tokenRepository.deleteByUsername(dto.username());
            Optional<User> userOptional = userRepository.findByLogin(dto.username());
            if (userOptional.isEmpty()) {
                throw new InvalidJwtException(USERNAME_NOT_EXIST);
            }
            if (passwordEncoder.matches(dto.password(), userOptional.get().getPasswordHash())) {
                return buildJwt(dto.username(), dto.password());
            }
            throw new InvalidJwtException(INVALID_TOKEN_ERROR);
        } catch (Exception e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    /**
     * Проверка прав доступа пользователя по JWT токену.
     *
     * @param token JWT токен
     * @return true, если у пользователя есть доступ, иначе false
     */
    @Override
    public boolean check(String token) {
        return checkToken(token);
    }

    /**
     * Проверяет валидность JWT токена.
     *
     * @param token JWT токен
     * @return true, если токен валиден, иначе false
     */
    private boolean checkToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);
            JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Метод для создания и возвращения JWT-токена
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @return объект JwtDto, содержащий JWT-токен
     */
    private JwtDto buildJwt(String login, String password) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login, password);
        var authUser = authenticationManager.authenticate(usernamePassword);
        var accessToken = tokenProvider.generateAccessToken((User) authUser.getPrincipal());
        return new JwtDto(accessToken);
    }
}