package ru.yandex.practicum.filmorate.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController extends ControllerEntity<User> {

    @GetMapping
    public List<User> getUsers() {
        return super.getStorage();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return super.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
       return super.update(user);
    }

    @Override
    public void validate(User user) throws NullPointerException {
        if (user.getEmail().isBlank() || user.getEmail().isEmpty()) {
            throw new ValidationException("E-mail адрес не может быть пустым.");
        } else if (!(user.getEmail().contains("@"))) {
            throw new ValidationException("E-mail адрес должен содержать символ @");
        } else if (user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        } else if (StringUtils.containsWhitespace(user.getLogin())) {
            throw new ValidationException("Логин не может содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}