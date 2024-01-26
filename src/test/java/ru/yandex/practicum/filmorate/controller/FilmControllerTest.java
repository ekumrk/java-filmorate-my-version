package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAddFilmAndUpdateFilm() throws Exception {
        //Добавление фильма
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Film name\", \"description\": \"Film description\"," +
                                " \"releaseDate\": \"1967-03-25\", \"duration\": 100}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films"))
                .andExpect(content().string("[{\"name\":\"Film name\",\"description\":\"Film description\"," +
                        "\"releaseDate\":\"1967-03-25\",\"duration\":100,\"id\":1,\"likes\":[]}]"));

        //Обновление фильма
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"1\",\"name\": \"FilmNameUpdate\", \"description\": \"Film description\"," +
                                " \"releaseDate\": \"1967-03-25\", \"duration\": 100}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films"))
                .andExpect(content().string("[{\"name\":\"FilmNameUpdate\",\"description\":\"Film description\"," +
                        "\"releaseDate\":\"1967-03-25\",\"duration\":100,\"id\":1,\"likes\":[]}]"));
    }

    @Test
    void shouldAddFilmAndUpdateFilmUnsuccessfully() throws Exception {
        //фильм с пустым названием
        mockMvc.perform(post("/films/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"description\": \"Film description\"," +
                                " \"releaseDate\": \"1967-03-25\", \"duration\": 100}"))
                .andExpect(status().is5xxServerError());

        //фильм со слишком длинным описанием
        mockMvc.perform(post("/films/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"FilmName\", \"description\": \"Film descriptionFilm descriptionFilm " +
                                "descriptionFilm descriptionFilm descriptionFilm descriptionFilm descriptionFilm " +
                                "descriptionFilm descriptionFilm descriptionFilm descriptionFilm descriptionFilm " +
                                "descriptionFilm descriptionFilm descriptionFilm descriptionFilm descriptionFilm " +
                                "descriptionFilm descriptionFilm description Film descriptionFilm descriptionFilm " +
                                "descriptionFilm descriptionFilm descriptionFilm descriptionFilm description\"," +
                                " \"releaseDate\": \"1967-03-25\", \"duration\": 100}"))
                .andExpect(status().is5xxServerError());

        //обновление несуществующего фильма
        mockMvc.perform(put("/films/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"2\",\"name\": \"FilmNameUpdate\", \"description\": \"Film description\"," +
                                " \"releaseDate\": \"1967-03-25\", \"duration\": 100}"))
                .andExpect(status().is4xxClientError());

        //фильм с неправильной датой выпуска
        mockMvc.perform(put("/films/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"FilmName\", \"description\": \"Film description\"," +
                                " \"releaseDate\": \"1890-03-25\", \"duration\": 100}"))
                .andExpect(status().is5xxServerError());
    }

}