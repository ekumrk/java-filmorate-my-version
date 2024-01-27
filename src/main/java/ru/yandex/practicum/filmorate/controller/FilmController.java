package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        filmService.create(film);
        log.debug("Фильм успешно добавлен {}: ", film.getName());
        return film;
    }

    @GetMapping()
    public List<Film> getFilms() {
        List<Film> allFilm = new ArrayList<>(filmService.getAll());
        log.debug("Количество фильмов: {}", allFilm.size());
        return allFilm;
    }

    @GetMapping("/{id}")
    public Optional<Film> getFilmById(@Validated @PathVariable @Min(1) int id) {
        Optional<Film> film = Optional.ofNullable(filmService.getById(id));

        if (film.isEmpty()) {
            throw new ValidationException("Такого фильма не существует");
        }
        log.debug("Фильм не найден в базе {}", film.get().getName());
        return film;
    }

    @GetMapping("/popular")
    public List<Film> findPopular(@RequestParam(defaultValue = "10") int count) {
        int filmCount = count;
        log.debug("Показаны лучшие фильмы.");
        return filmService.getTopFilm(filmCount);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        filmService.update(film);
        log.debug("Фильм успешно обновлен: {} ", film.getName());
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        if (userId < 0) {
            throw new DataNotFoundException("Некорректный ID пользователя;");
        }
        filmService.addLike(id, userId);
        log.debug("Пользователь с id = {} поставил лайк фильму {}: ", userId, filmService.getById(id).getName());
    }


    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeToFilm(@PathVariable int id, @PathVariable int userId) throws ValidationException {
        if (userId < 0) {
            throw new ValidationException("Некорректный ID пользователя;");
        }
        filmService.removeLike(userId, id);
        log.debug("Пользователь {} удалил лайк к фильму {}: ", userId, filmService.getById(id).getName());
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable int id) {
        filmService.delete(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationException handleNullableCount(final ValidationException e) {
        return new ValidationException("Ошибка учетного параметра");
    }
}
