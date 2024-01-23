package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    public void validate(User user);

    public User getUser(int id);

    Map<Integer, User> getUsers();
}
