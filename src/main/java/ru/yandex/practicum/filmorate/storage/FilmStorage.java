package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.supportive.Genre;

import java.util.List;
import java.util.Set;

public interface FilmStorage {

    public Film create(Film film);

    public Film update(Film film);

    public Film getFilm(int id);

    List<Film> getFilms();

    public String delete(int filmId);

    void addGenre(int filmId, Set<Genre> genres);
}
