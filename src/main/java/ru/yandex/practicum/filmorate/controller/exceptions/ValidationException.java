package ru.yandex.practicum.filmorate.controller.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }
}
