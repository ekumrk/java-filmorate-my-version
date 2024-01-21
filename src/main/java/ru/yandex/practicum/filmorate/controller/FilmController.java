package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

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
    private final InMemoryFilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film postRequestFilm(@Valid @RequestBody Film film) {
        filmStorage.create(film);
        log.debug("The movie is added{}: ", film.getName());
        return film;
    }

    @GetMapping()
    public List<Film> getRequestAllFilm() {
        List<Film> allFilm = new ArrayList<>(filmStorage.getFilms().values());
        log.debug("Amount of movies: {}", allFilm.size());
        return allFilm;
    }

    @GetMapping("/{id}")
    public Optional<Film> getRequestFilm(@PathVariable @Min(1) int id) {
        Optional<Film> film = Optional.ofNullable(filmStorage.getFilm(id));

        if (film.isEmpty()) {
            throw new ValidationException("Такого фильма не существует");
        }
        log.debug("The movie is found {}", film.get().getName());
        return film;
    }

    @GetMapping("/popular")
    public List<Film> getRequestTopFilm(@RequestParam(value = "count", defaultValue = "10") int count) {
        log.debug("Show top films.");
        return filmService.getTenMostPopularFilm(count);
    }

    @PutMapping
    public Film putRequestFilm(@Valid @RequestBody Film film) {
        filmStorage.update(film);
        log.debug("The movie is changed: {} ", film.getName());
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void putRequestAddLike(@PathVariable @Min(1) int id, @PathVariable int userId) {
        if (userId < 0) {
            throw new DataNotFoundException("Некорректный ID пользователя;");
        }
        filmService.addLike(userId, id);
        log.debug("User {} added a like to the movie {}: ", userId, filmStorage.getFilm(id).getName());
    }


    @DeleteMapping("/{id}/like/{userId}")
    public void deleteRequestDeleteLike(@PathVariable @Min(1) int id, @PathVariable int userId) {
        if (userId < 0) {
            throw new DataNotFoundException("Некорректный ID пользователя;");
        }
        filmService.removeLike(userId, id);
        log.debug("Пользователь {} удалил лайк к фильму {}: ", userId, filmStorage.getFilm(id).getName());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationException handleNullableCount(final ValidationException e) {
        return new ValidationException("Ошибка учетного параметра");
    }
}
