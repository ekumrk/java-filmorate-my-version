package ru.yandex.practicum.filmorate.service.supportive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.supportive.Genre;
import ru.yandex.practicum.filmorate.storage.daoimpl.GenreDbStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Genre getGenreById(int id) {
        Genre genre = genreDbStorage.getGenreById(id);
        if (genre == null) {
            throw new DataNotFoundException("Genre not found");
        }
        return genre;
    }

    public List<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }
}