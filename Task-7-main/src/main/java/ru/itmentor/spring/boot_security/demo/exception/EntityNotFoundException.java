package ru.itmentor.spring.boot_security.demo.exception;

import static ru.itmentor.spring.boot_security.demo.constant.Constant.ENTITY_NOT_FOUND_EXCEPTION;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException() {
        super(ENTITY_NOT_FOUND_EXCEPTION);
    }
}