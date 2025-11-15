package ru.itmentor.spring.boot_security.demo.service.impl;

import ru.itmentor.spring.boot_security.demo.converter.UserConverter;
import ru.itmentor.spring.boot_security.demo.exception.EntityNotFoundException;
import ru.itmentor.spring.boot_security.demo.exception.InvalidJwtException;
import ru.itmentor.spring.boot_security.demo.model.dto.UserDto;
import ru.itmentor.spring.boot_security.demo.model.dto.create.UserCreateDto;
import ru.itmentor.spring.boot_security.demo.model.dto.update.UserUpdateDto;
import ru.itmentor.spring.boot_security.demo.model.entity.BlackList;
import ru.itmentor.spring.boot_security.demo.model.entity.User;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repository.TokenRepository;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;
import ru.itmentor.spring.boot_security.demo.service.UserService;
import ru.itmentor.spring.boot_security.demo.util.TokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static ru.itmentor.spring.boot_security.demo.constant.Constant.USERNAME_IS_EXIST;

/**
 * Реализация сервиса управления пользователями.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final TokenUtil tokenUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * Находит текущего пользователя по токену авторизации.
     *
     * @param auth токен авторизации для получения текущего пользователя.
     * @return объект {@link UserDto} с данными текущего пользователя.
     * @throws EntityNotFoundException если пользователь не найден.
     */
    @Override
    public UserDto findCurrentUser(String auth) {
        return userConverter.convert(userRepository.findByLogin(tokenUtil.getUsername(auth)).orElseThrow(EntityNotFoundException::new));
    }

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return объект {@link UserDto} с данными пользователя.
     * @throws EntityNotFoundException если пользователь не найден.
     */
    @Override
    public UserDto findById(Long id) {
        return userConverter.convert(userRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    /**
     * Получает список всех пользователей.
     *
     * @return список объектов {@link UserDto} с данными пользователей.
     */
    @Override
    public List<UserDto> findAll() {
        var userList = userRepository.findAll();
        return userList.stream().map(userConverter::convert).collect(Collectors.toList());
    }

    /**
     * Создает нового пользователя.
     *
     * @param dto данные для создания нового пользователя.
     * @return объект {@link UserDto} с данными созданного пользователя.
     * @throws InvalidJwtException если пользователь с таким логином уже существует.
     */
    @Override
    @Transactional
    public UserDto create(UserCreateDto dto) {
        if (Boolean.TRUE.equals(userRepository.existsUserByLogin(dto.getUsername()))) {
            throw new InvalidJwtException(USERNAME_IS_EXIST);
        }
        var roleSet = dto.getRoleList().stream()
                .map(roleDto -> roleRepository.findByName(roleDto.getName()).orElseThrow(EntityNotFoundException::new))
                .collect(Collectors.toSet());
        var encryptedPassword = passwordEncoder.encode(dto.getPassword());
        var newUser = new User(dto.getUsername(), encryptedPassword, roleSet);
        return userConverter.convert(userRepository.save(newUser));
    }

    /**
     * Обновляет информацию о существующем пользователе.
     *
     * @param id  id пользователя
     * @param dto объект {@link UserUpdateDto} с новыми данными пользователя.
     * @return объект {@link UserDto} с обновлёнными данными пользователя.
     * @throws EntityNotFoundException если пользователь не найден.
     */
    @Override
    public UserDto update(Long id, UserUpdateDto dto) {
        var user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return userConverter.convert(userRepository.save(userConverter.merge(user, dto)));
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя, которого нужно удалить.
     */
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Обрабатывает выход пользователя из системы, добавляя токен в черный список.
     *
     * @param token токен аутентификации, который пользователь хочет отозвать
     * @throws EntityNotFoundException если пользователь с указанным логином не найден
     */
    @Override
    public void logout(String token) {
        User user = userRepository.findByLogin(tokenUtil.getUsername(token)).orElseThrow(EntityNotFoundException::new);
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(tokenUtil.getExt(token)), ZoneId.systemDefault());
        tokenRepository.save(new BlackList(user.getLogin(), token, dateTime));
    }
}