package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @RequestMapping("/newFilm")
    @PostMapping (value = "film")
    public Film createFilm(@RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @RequestMapping("/updateFilm")
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }
}
