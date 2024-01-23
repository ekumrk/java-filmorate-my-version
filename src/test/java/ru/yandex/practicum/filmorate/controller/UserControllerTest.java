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
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerTest {
    private UserController u;
    @Autowired
    private InMemoryUserStorage userStorage;
    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    public static final String PATH = "/users";

    @BeforeEach
    void setUp() {
        u = new UserController();
    }

    @Test
    void validateIsOk() {
        User user = User.builder()
                .email("test@yandex.ru")
                .login("testlogin")
                .name("vasya testov")
                .birthday(LocalDate.of(2000, 5, 6))
                .build();
        userStorage.create(user);
        Assertions.assertFalse(userStorage.getUsers().isEmpty());
    }

    @Test
    void throwsExceptionDuringValidationIfEmailIsBlankOrEmpty() {
        User user = User.builder()
                .email("")
                .login("testlogin")
                .name("vasya testov")
                .birthday(LocalDate.of(2000, 5, 6))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userStorage.validate(user));
    }

    @Test
    void throwsExceptionDuringValidationIfEmailDoesntContainCharDog() {
        User user = User.builder()
                .email("test-yandex.ru")
                .login("testlogin")
                .name("vasya testov")
                .birthday(LocalDate.of(2000, 5, 6))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userStorage.validate(user));
    }

    @Test
    void throwsExceptionIfDuringValidationLoginIsEmpty() {
        User user = User.builder()
                .email("test@yandex.ru")
                .login("")
                .name("vasya testov")
                .birthday(LocalDate.of(2000, 5, 6))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userStorage.validate(user));
    }

    @Test
    void throwsExceptionIfLoginContainsSpaces() {
        User user = User.builder()
                .email("test@yandex.ru")
                .login("vasya petrov")
                .name("vasya testov")
                .birthday(LocalDate.of(2000, 5, 6))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userStorage.validate(user));
    }

    @Test
    void throwsExceptionIfDateOfBirthdayIsInFuture() {
        User user = User.builder()
                .email("test@yandex.ru")
                .login("testlogin")
                .name("vasya testov")
                .birthday(LocalDate.of(2030, 5, 6))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userStorage.validate(user));
    }

    @Test
    void createNewUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readStringFromFile("controller/request/user.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        readStringFromFile("controller/response/user.json")
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