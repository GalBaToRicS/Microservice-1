package ru.itmentor.spring.boot_security.demo.config.service;

import ru.itmentor.spring.boot_security.demo.model.entity.User;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static ru.itmentor.spring.boot_security.demo.constant.Constant.ENTITY_NOT_FOUND_EXCEPTION;

/**
 * Реализация интерфейса {@link UserDetailsService} для загрузки данных пользователя
 * из репозитория пользователей.
 * <p>
 * Этот класс помечен аннотацией {@link Component}, чтобы позволить автоматическое
 * обнаружение и регистрацию в рамках сканирования компонентов Spring.
 * <p>
 * Он использует {@link UserRepository} для получения информации о пользователе по
 * имени пользователя.
 */
@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает данные пользователя по имени пользователя.
     * <p>
     * Этот метод проверяет, существует ли пользователь с указанным именем в
     * репозитории пользователей. Если пользователь найден, возвращается
     * {@link UserDetails} пользователя. Если пользователь не найден, выбрасывается
     * {@link UsernameNotFoundException}.
     *
     * @param username имя пользователя, данные которого необходимо получить
     * @return {@link UserDetails} запрашиваемого пользователя
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByLogin(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UsernameNotFoundException(ENTITY_NOT_FOUND_EXCEPTION);
        }
    }
}