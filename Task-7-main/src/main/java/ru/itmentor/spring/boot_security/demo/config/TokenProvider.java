package ru.itmentor.spring.boot_security.demo.config;

import ru.itmentor.spring.boot_security.demo.constant.RoleName;
import ru.itmentor.spring.boot_security.demo.model.entity.Role;
import ru.itmentor.spring.boot_security.demo.model.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import static ru.itmentor.spring.boot_security.demo.constant.Constant.GENERATION_TOKEN_ERROR;

/**
 * Класс, отвечающий за генерацию и проверку токенов аутентификации.
 * <p>
 * Этот класс предоставляет методы для создания токенов доступа и определения
 * их срока действия. Использует библиотеку JWT для обработки токенов.
 */
@Component
public class TokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String jwtSecret;

    /**
     * Генерирует токен доступа для пользователя.
     * <p>
     * Этот метод создает JWT токен, содержащий информацию о пользователе, его роли
     * и дату истечения срока действия токена.
     *
     * @param user Пользователь, для которого генерируется токен доступа.
     * @return Сгенерированный токен доступа.
     * @throws JWTCreationException Если произошла ошибка при генерации токена.
     */
    public String generateAccessToken(User user) {
        try {
            List<String> roles = user.getRoleList().stream()
                    .map(Role::getName)
                    .map(RoleName::name)
                    .collect(Collectors.toList());
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("username", user.getUsername())
                    .withClaim("roles", roles)
                    .withExpiresAt(genAccessExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException(GENERATION_TOKEN_ERROR, exception);
        }
    }

    /**
     * Генерирует дату истечения срока действия токена доступа.
     * <p>
     * Этот метод определяет, когда токен доступа станет недействительным,
     * добавляя два часа к текущему времени.
     *
     * @return Дата истечения срока действия токена доступа.
     */
    private Instant genAccessExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
