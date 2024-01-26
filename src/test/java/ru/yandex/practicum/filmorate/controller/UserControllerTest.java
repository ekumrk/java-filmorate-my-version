package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mvc;

    @Test
    void createUserWithInvalidLogin() throws Exception {
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore ullamco\",\n" +
                                "  \"email\": \"yandex@mail.ru\",\n" +
                                "  \"birthday\": \"2446-08-20\"\n" +
                                "}"))
                .andExpect(status().is(400));
    }


    @Test
    void createUserWithInvalidEmail() throws Exception {
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore ullamco\",\n" +
                                "  \"name\": \"\",\n" +
                                "  \"email\": \"mail.ru\",\n" +
                                "  \"birthday\": \"1980-08-20\"\n" +
                                "}"))
                .andExpect(status().is(400));
    }


    @Test
    void createUserWithInvalidBirthday() throws Exception {
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore ullamco\",\n" +
                                "  \"name\": \"\",\n" +
                                "  \"email\": \"mail.ru\",\n" +
                                "  \"birthday\": \"1980-08-20\"\n" +
                                "}"))
                .andExpect(status().is(400));
    }
}