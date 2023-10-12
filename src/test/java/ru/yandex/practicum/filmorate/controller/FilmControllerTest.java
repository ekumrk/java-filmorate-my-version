package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FilmControllerTest {

    public static final String PATH = "/films";
    @Autowired
    private MockMvc mockMvc;
    private FilmController f;

    @BeforeEach
    void setUp() {
        f = new FilmController();
    }

    @Test
    void validationOk() {
        Film film = Film.builder()
                .name("Harry Potter")
                .description("Best film")
                .releaseDate(LocalDate.of(2020, 3, 12))
                .duration(120)
                .build();
        f.createFilm(film);
        Assertions.assertFalse(f.getFilms().isEmpty());
    }

    @Test
    void throwsExceptionDuringValidationIfNameIsBlank() {
        Film film = Film.builder()
                .name("")
                .description("Best film")
                .releaseDate(LocalDate.of(2020, 3, 12))
                .duration(120)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> f.validate(film));
    }

    @Test
    void throwsExceptionIfDescriptionLengthIsMoreThan200Chars() {
        Film film = Film.builder()
                .name("Chelovek-Pauk")
                .description("Good film aaaaaaaaaaaaagofgjiodenfjklgoiebnowseofhihboiwehrouvbjklsebdnouifbvqoebroivboi" +
                        "njksefkbjvjiebrviuqbejkrbvjksbdkjbvukbwerkbuvuiwheuirgvquiehriuohqweefoiqwerouhfqiowernbofqw" +
                        "qwerfjoqberobvquiewbruifbquiwoebfuibquiewbruiqbeuwivbiuqbwuiebvuiqberwuivbquiwbeuivbquibqiuweb")
                .releaseDate(LocalDate.of(2020,3,03))
                .duration(120)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> f.validate(film));
    }

    @Test
    void throwsExceptionIfReleaseDateIsBeforeAllowed() {
        Film film = Film.builder()
                .name("Harry Potter")
                .description("Best film")
                .releaseDate(LocalDate.of(1800, 3, 12))
                .duration(120)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> f.validate(film));
    }

    @Test
    void throwsExceptionIfDurationIsNegative() {
        Film film = Film.builder()
                .name("Harry Potter")
                .description("Best film")
                .releaseDate(LocalDate.of(1999, 3, 12))
                .duration(-120)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> f.validate(film));
    }

    @Test
    void createNewFilm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(readStringFromFile("controller/request/film.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        readStringFromFile("controller/response/film.json")
                ));
    }

    private String readStringFromFile(String filename) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + filename).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException exception) {
            return "";
        }
    }
}