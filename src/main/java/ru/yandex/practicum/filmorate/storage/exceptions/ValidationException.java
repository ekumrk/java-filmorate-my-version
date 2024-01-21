package ru.yandex.practicum.filmorate.storage.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }
}
