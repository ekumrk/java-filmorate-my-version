package ru.yandex.practicum.filmorate.model.supportive;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private final String error;
}