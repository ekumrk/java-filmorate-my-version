package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @GetMapping
    public List<Film> getUsers() {
        List<Film> filmList = new ArrayList<>();
        for (Film f: films.values()) {
            filmList.add(f);
        }
        return filmList;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
            validate(film);

            film.setId(currentId);
            films.put(currentId, film);
            log.debug("Фильм успешно добавлен. '{}'", film);
            currentId++;
            return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Фильм успешно обновлён. '{}'", film);
            return film;
        } else {
            throw new ValidationException("Нет фильма с таким id");
        }
    }

    private void validate(Film film) throws NullPointerException {
       if (film.getName().isBlank()) {
           throw new ValidationException("Название фильма не может быть пустым.");
       } else if (film.getDescription().length() > 200) {
           throw new ValidationException("Описание фильма не может содержать более 200 символов.");
       } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12,28))) {
           throw new ValidationException("Дата релиза фильма не может быть ранее 28 декабря 1895 года");
       } else if (film.getDuration() <= 0) {
           throw new ValidationException("Продолжительность фильма должна быть положительной.");
       } else if (film.equals(null)) {
           throw new ValidationException("Не все поля были заполнены при оформлении фильма.");
       }
    }
}
