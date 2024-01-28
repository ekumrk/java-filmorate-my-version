package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    private final LikesStorage likesStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, @Qualifier("UserDbStorage") UserStorage
            userStorage, @Qualifier("LikesDbStorage") LikesStorage likesStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.likesStorage = likesStorage;
    }

    public List<Film> getAll() {
        log.info("Список фильмов: " + filmStorage.getFilms().size());
        return filmStorage.getFilms();
    }

    public Film create(Film film) {
        Film result = filmStorage.create(film);
        log.info("Фильм успешно добавлен: " + film);
        return result;
    }

    public Film update(Film film) {
        Film result = filmStorage.update(film);
        log.info("Movie successfully updated: " + film);
        return result;
    }

    public void delete(int filmId) {
        if (getById(filmId) == null) {
            throw new DataNotFoundException("Фильм с Id = " + filmId + " не найден");
        }
        log.info("Удалён фильм с id: {}", filmId);
        filmStorage.delete(filmId);
    }

    public Film getById(int id) {
        log.info("Запрошен фильм с id = " + id);
        return filmStorage.getFilm(id);
    }

    public void addLike(int userId, int filmId) {
        Film film = filmStorage.getFilm(filmId);
        if (film != null) {
            if (userStorage.getUser(userId) != null) {
                likesStorage.addLike(filmId, userId);
                log.info("Like successfully added");
            } else {
                throw new DataNotFoundException("Пользователь с id = " + userId + " не найден");
            }
        } else {
            throw new DataNotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film != null) {
            if (userStorage.getUser(userId) != null) {
                likesStorage.removeLike(filmId, userId);
                log.info("Like successfully removed");
            } else {
                throw new DataNotFoundException("Пользователь с id = " + userId + " не найден");
            }
        } else {
            throw new DataNotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }

    public List<Film> getTopFilm(int count) {
        return likesStorage.getTopFilm(count);
    }
}