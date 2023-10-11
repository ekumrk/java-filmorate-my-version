package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @GetMapping
    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        for (User u: users.values()) {
            userList.add(u);
        }
        return userList;
    }
    //Согласен, что можно вернуть мапу и всё, но так тесты в постмане не пропускают(

    @PostMapping
    public User addUser(@RequestBody User user) {
        validate(user);

        if (user.name == null || user.name.isBlank()) {
            user.name = user.getLogin();
        }
        user.setId(currentId);
        users.put(currentId, user);
        log.debug("Пользователь успешно добавлен. '{}'", user);
        currentId++;
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Пользователь успешно обновлён. '{}'", user);
            return user;
        } else {
            throw new ValidationException("Нет пользователя с таким id");
        }
    }

    private void validate(User user) throws NullPointerException {
        if (user.getEmail().isBlank() || user.getEmail().isEmpty()){
            throw new ValidationException("E-mail адрес не может быть пустым.");
        } else if (!(user.getEmail().contains("@"))) {
            throw new ValidationException(("E-mail адрес должен содержать символ @"));
        } else if (user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        } else if (StringUtils.containsWhitespace(user.getLogin())) {
            throw new ValidationException("Логин не может содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
