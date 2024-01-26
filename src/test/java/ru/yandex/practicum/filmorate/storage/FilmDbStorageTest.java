package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.supportive.MPA;
import ru.yandex.practicum.filmorate.storage.daoimpl.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    private Film testFilm1 = Film.builder()
            .name("filmname")
            .description("description")
            .duration(120)
            .releaseDate(LocalDate.of(2010, 12, 12))
            .mpa(new MPA(1,"PG"))
            .build();

    private Film testFilm2 = Film.builder()
            .name("filmname2")
            .description("description2")
            .duration(150)
            .mpa(new MPA(3,"PG-13"))
            .releaseDate(LocalDate.of(2020, 8, 18))
            .build();

    @Test
    public void findFilmById() {
        filmDbStorage.create(testFilm1);
        testFilm1.setId(1);
        Assertions.assertEquals(testFilm1.getName(), filmDbStorage.getFilm(1).getName());
    }

    @Test
    public void shouldThrowExceptionWhenInvalidId() {
        DataNotFoundException e = Assertions.assertThrows(
                DataNotFoundException.class,
                () -> filmDbStorage.getFilm((10)
        ));
        Assertions.assertEquals("Фильм с id 10 не найден", e.getMessage());
    }

    @Test
    public void findAll() {
        filmDbStorage.create(testFilm1);
        filmDbStorage.create(testFilm2);
        List<Film> films = new ArrayList<>(filmDbStorage.getFilms());
        Assertions.assertEquals(testFilm1.getName(), films.get(0).getName());
        Assertions.assertEquals(testFilm2.getName(), films.get(1).getName());
    }
}
