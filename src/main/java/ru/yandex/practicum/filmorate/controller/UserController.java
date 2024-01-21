package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private InMemoryUserStorage userStorage;
    private UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public User postRequestUser(@Valid @RequestBody User user) {
        userStorage.create(user);
        return user;
    }

    @GetMapping()
    public List<User> getRequestAllUser() {
        List<User> allUsers = new ArrayList<>(userStorage.getUsers().values());
        log.debug("Amount of movies: {}", allUsers.size());
        return allUsers;
    }

    @GetMapping("/{id}")
    public Optional<User> getRequestUser(@PathVariable @Min(1) int id) {
        Optional<User> user = Optional.ofNullable(userStorage.getUser(id));

        if (user.isEmpty()) {
            throw new ValidationException("Такого пользователя не существует");
        }

        log.debug("The user is found{}: ", user.get().getName());
        return user;
    }

    @GetMapping("/{id}/friends")
    public List<User> getRequestAllUserFriends(@PathVariable @Min(1) int id) {
        log.debug("Show all friends.");
        return userService.listAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getRequestOfMutualFriends(@PathVariable @Min(1) int id, @PathVariable @Min(1) int otherId) {
        log.debug("Show all mutual friends.");
        return userService.listOfMutualFriends(id, otherId);
    }

    @PutMapping
    public User putRequestUser(@Valid @RequestBody User user) {
        userStorage.update(user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void putRequestAddFriend(@PathVariable @Min(1) int id, @PathVariable int friendId) throws ValidationException {
        if (friendId < 0) {
            throw new ValidationException("Некорректный номер друга");
        }
        userService.addFriend(id, friendId);
        log.debug("User {} added to friend {}", userStorage.getUser(id).getName(),
                userStorage.getUser(friendId).getName());
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteRequestDeleteFriend(@PathVariable @Min(1) int id, @PathVariable int friendId) {
        userService.removeFriend(id, friendId);
        log.debug("User {} delete to friend {}", userStorage.getUser(id).getName(),
                userStorage.getUser(friendId).getName());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationException handleNullableCount(final ValidationException e) {
        return new ValidationException("Ошибка с параметром coun");
    }
}
