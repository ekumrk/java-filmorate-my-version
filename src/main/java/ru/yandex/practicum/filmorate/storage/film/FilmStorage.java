package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    public void validate(Film film);

    public Film getFilm(int id);

    Map<Integer, Film> getFilms();
}
