package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.EntityStorage;
import ru.yandex.practicum.filmorate.storage.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage extends EntityStorage<Film> implements FilmStorage {

    @Autowired
    public InMemoryFilmStorage() {
        this.storage = new HashMap<>();
    }

    public void validate(Film film) throws NullPointerException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не может содержать более 200 символов.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть ранее 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

    @Override
    public Film getFilm(int id) {
        return storage.get(id);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return storage;
    }
}
