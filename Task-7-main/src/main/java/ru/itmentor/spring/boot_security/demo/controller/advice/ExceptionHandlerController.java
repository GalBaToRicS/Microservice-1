package ru.itmentor.spring.boot_security.demo.controller.advice;

import ru.itmentor.spring.boot_security.demo.exception.EntityNotFoundException;
import ru.itmentor.spring.boot_security.demo.exception.InvalidJwtException;
import ru.itmentor.spring.boot_security.demo.model.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Контроллер обработки исключений.
 * <p>
 * Этот класс предназначен для перехвата и обработки исключений,
 * возникающих в приложении, и формирования соответствующих
 * ответов с информацией об ошибках.
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * Обработчик исключений для случаев, когда запрашиваемый ресурс не найден.
     *
     * @param e исключение {@link EntityNotFoundException}, содержащее информацию о причине ошибки.
     * @return объект {@link ErrorDto} с сообщением об ошибке и статусом 404 (Not Found).
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto error(EntityNotFoundException e) {
        return new ErrorDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    /**
     * Обработчик исключений для недействительных JWT токенов.
     *
     * @param e исключение {@link InvalidJwtException}, содержащее информацию о причине ошибки.
     * @return объект {@link ErrorDto} с сообщением об ошибке и статусом 500 (Internal Server Error).
     */
    @ExceptionHandler(InvalidJwtException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto error(InvalidJwtException e) {
        return new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Общий обработчик исключений для всех остальных случаев.
     * <p>
     * Этот метод ловит все остальные исключения и возвращает общий
     * ответ об ошибке.
     *
     * @param e общее исключение {@link Exception}, которое может произойти в приложении.
     * @return объект {@link ErrorDto} с сообщением об ошибке и статусом 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto error(Exception e) {
        return new ErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}