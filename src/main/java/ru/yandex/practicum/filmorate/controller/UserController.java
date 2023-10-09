package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Map<Integer, User> getUsers() {
        return users;
    }

    @PostMapping (value = "user")
    @RequestMapping("/addUser")
    public User addUser(@RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    @RequestMapping("/updateUser")
    public User updateUser(@RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }
}
