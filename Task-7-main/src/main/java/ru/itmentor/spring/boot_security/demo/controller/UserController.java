package ru.itmentor.spring.boot_security.demo.controller;

import ru.itmentor.spring.boot_security.demo.model.dto.UserDto;
import ru.itmentor.spring.boot_security.demo.model.dto.create.UserCreateDto;
import ru.itmentor.spring.boot_security.demo.model.dto.update.UserUpdateDto;
import ru.itmentor.spring.boot_security.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ru.itmentor.spring.boot_security.demo.constant.Constant.AUTHORIZATION_HEADER;

/**
 * Контроллер для управления пользователями.
 * <p>
 * Этот контроллер обрабатывает HTTP-запросы, связанные с пользователями,
 * включая операции получения, создания, обновления и удаления пользователей.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Получает информацию о текущем пользователе.
     *
     * @param auth Заголовок авторизации, содержащий токен текущего пользователя.
     * @return объект {@link ResponseEntity} с данными о пользователе и статусом 200 (OK).
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> findCurrentUserById(@RequestHeader("Authorization") String auth) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findCurrentUser(auth));
    }

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return объект {@link ResponseEntity} с данными о пользователе и статусом 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findById(id));
    }

    /**
     * Получает список всех пользователей.
     *
     * @return объект {@link ResponseEntity} со списком пользователей и статусом 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findAll());
    }

    /**
     * Создает нового пользователя.
     *
     * @param personCreateDto Данные для создания нового пользователя.
     * @return объект {@link ResponseEntity} с созданным пользователем и статусом 201 (Created).
     */
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserCreateDto personCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(personCreateDto));
    }

    /**
     * Обновляет информацию о существующем пользователе.
     *
     * @param id              id обновления пользователя.
     * @param personUpdateDto Данные для обновления пользователя.
     * @return объект {@link ResponseEntity} с обновленным пользователем и статусом 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") Long id, @RequestBody UserUpdateDto personUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.update(id, personUpdateDto));
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя, которого необходимо удалить.
     * @return объект {@link ResponseEntity} с статусом 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Обрабатывает запрос на выход пользователя (logout) из системы.
     *
     * @param auth токен аутентификации, переданный в заголовке запроса
     * @return экземпляр {@link ResponseEntity}, представляющий ответ HTTP
     */
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(AUTHORIZATION_HEADER) String auth) {
        userService.logout(auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}