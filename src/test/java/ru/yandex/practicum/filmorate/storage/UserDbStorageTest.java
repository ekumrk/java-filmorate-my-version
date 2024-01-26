package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.daoimpl.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private User testUser1 = User.builder()
            .name("Username1")
            .email("email1")
            .login("login1")
            .birthday(LocalDate.of(1999, 3, 15))
            .build();

    private User testUser2 = User.builder()
            .name("Username2")
            .email("email2")
            .login("login2")
            .birthday(LocalDate.of(1996, 5, 10))
            .build();


    @Test
    public void findUserById() {
        long userId = userDbStorage.create(testUser1).getId();
        Assertions.assertEquals(testUser1.getName(), userDbStorage.getUser((int) userId));
    }

    @Test
    public void findAllUsers() {
        userDbStorage.create(testUser1).getId();
        userDbStorage.create(testUser2).getId();
        ArrayList<User> users = new ArrayList<>(userDbStorage.getUsers());
        Assertions.assertEquals(testUser1.getName(), users.get(0).getName());
        Assertions.assertEquals(testUser2.getName(), users.get(1).getName());
    }

    @Test
    public void shouldThrowExceptionWhenInvalidId() {
        DataNotFoundException ex = Assertions.assertThrows(
                DataNotFoundException.class,
                () -> userDbStorage.getUser(10)
        );
        Assertions.assertEquals("Пользователь с Id 10 не найден", ex.getMessage());
    }

    @Test
    public void shouldUseLoginIfNameIsEmpty() {
        User testUser3 = User.builder()
                .email("email1")
                .login("login1")
                .birthday(LocalDate.of(1999, 3, 15))
                .build();
        int userId = userDbStorage.create(testUser3).getId();
        User userFromDb = userDbStorage.getUser(userId);
        Assertions.assertEquals("login1", userFromDb.getName());
    }
}
