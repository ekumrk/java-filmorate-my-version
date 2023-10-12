package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController extends ControllerEntity<Film> {

    @GetMapping
    public List<Film> getFilms() {
        return super.getStorage();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return super.create(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return super.update(film);
    }

    @Override
    public void validate(Film film) throws NullPointerException {
       if (film.getName().isBlank()) {
           throw new ValidationException("Название фильма не может быть пустым.");
       } else if (film.getDescription().length() > 200) {
           throw new ValidationException("Описание фильма не может содержать более 200 символов.");
       } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12,28))) {
           throw new ValidationException("Дата релиза фильма не может быть ранее 28 декабря 1895 года");
       } else if (film.getDuration() <= 0) {
           throw new ValidationException("Продолжительность фильма должна быть положительной.");
       }
    }
}
