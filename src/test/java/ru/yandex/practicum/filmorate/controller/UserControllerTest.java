package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAddAndUpdateUserSuccessfully() throws Exception {
        //пользователь с пустым именем
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"dolore\", \"name\": \"\", \"email\": \"mail@mail.ru\"," +
                                " \"birthday\": \"1946-08-20\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users"))
                .andExpect(content().string("[{\"email\":\"mail@mail.ru\",\"login\":\"dolore\"," +
                        "\"name\":\"dolore\",\"birthday\":\"1946-08-20\",\"id\":1,\"friends\":[]}]"));

        //Обновление пользователя
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"mail@mail.ru\",\"login\":\"dolore\",\"name\":\"doloreUpdate\"," +
                                "\"birthday\":\"1946-08-20\",\"id\":1}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users"))
                .andExpect(content().string("[{\"email\":\"mail@mail.ru\",\"login\":\"dolore\"," +
                        "\"name\":\"doloreUpdate\",\"birthday\":\"1946-08-20\",\"id\":1,\"friends\":[]}]"));
    }

    @Test
    void shouldAddUserAndUpdateUserUnsuccessfully() throws Exception {
        //пользователь с неправильным email
        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\": \"dolore\",\"email\": \"mailmail.ru\"," +
                        " \"birthday\": \"1946-08-20\"}")).andExpect(status().is5xxServerError());


        //обновление несуществующего пользователя
        mockMvc.perform(put("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"doloreUpdate\",\"name\": \"est adipisicing\",\"id\": 3," +
                                "\"email\": \"mail@yandex.ru\",\"birthday\": \"1976-09-20\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertEquals("Ошибка! Такого пользователя не существует!",
                        result.getResolvedException().getMessage()));

        //пользователь с неправильным email
        mockMvc.perform(MockMvcRequestBuilders.post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\": \"dolore\",\"email\": \"mailmail.ru\"," +
                        " \"birthday\": \"1946-08-20\"}")).andExpect(status().is5xxServerError());
    }

}