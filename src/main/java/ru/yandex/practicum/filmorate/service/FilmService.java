package ru.yandex.practicum.filmorate.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    public final InMemoryFilmStorage filmStorage;
    public final InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int idUser, int idFilm) {
        checkingTheExistenceOfUsersAndFilms(idUser, idFilm);

        filmStorage.getFilm(idFilm).addLike(idUser);
    }

    public void removeLike(int idUser, int idFilm) {
        checkingTheExistenceOfUsersAndFilms(idUser, idFilm);

        filmStorage.getFilm(idFilm).removeLike(idUser);
    }

    public List<Film> getTenMostPopularFilm(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted((f1, f2) -> {
                    if (f1.getLikes() == f2.getLikes()) {
                        return 0;
                    }
                    return (f1.getLikes() > f2.getLikes()) ? -1 : 1;
                })
                .limit(count)
                .collect(Collectors.toUnmodifiableList());
    }

    @SneakyThrows
    private void checkingTheExistenceOfUsersAndFilms(int idUser, int idFilm) {
        if (!userStorage.getUsers().containsKey(idUser)) {
            throw new ValidationException("Пользователь не найден");
        }
        if (!filmStorage.getFilms().containsKey(idFilm)) {
            throw new ValidationException("Такой фильм не существует, проверьте корректность введенного Id");
        }
    }
}
