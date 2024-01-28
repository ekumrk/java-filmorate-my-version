package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Component
@Service
@Slf4j
public class UserService {
    private final UserStorage storage;

    public UserService(@Qualifier("UserDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    public Collection<User> getAll() {
        log.info("Список пользователей: " + storage.getUsers().size());
        return storage.getUsers();
    }

    public User create(User user) {
        changeName(user);
        User result = storage.create(user);
        log.info("Пользователь успешно добавлен: " + user);
        return result;
    }

    public User update(User user) {
        changeName(user);
        User result = storage.update(user);
        log.info("Пользователь успешно обновлён: " + user);
        return result;
    }

    public void delete(int userId) {
        if (getById(userId) == null) {
            throw new DataNotFoundException("Пользователь с id = " + userId + " не найден");
        }
        log.info("Удалён пользователь с id: {}", userId);
        storage.delete(userId);
    }

    public User getById(int id) {
        log.info("Запрошен пользователь с id = " + id);
        return storage.getUser(id);
    }

    public void addFriend(int userId, int friendId) {
        checkUser(userId, friendId);
        storage.addFriend(userId, friendId);

        log.info("Друг успешно добавлен");
    }

    public void removeFriend(int userId, int friendId) {
        checkUser(userId, friendId);
        storage.removeFriend(userId, friendId);
        log.info("Друг успешно удалён");
    }

    public List<User> getAllFriends(int userId) {
        checkUser(userId, userId);
        List<User> result = storage.getFriendsById(userId);
        log.info("Друзья пользователя с id = " + userId + result);
        return result;
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        checkUser(userId, friendId);
        List<User> result = storage.getCommonFriends(userId, friendId);
        log.info("Общие друзья пользователей с id " + " {} и {} такие: {} ", userId, friendId, result);
        return result;
    }

    private void changeName(User user) {
        if (user.getName() == null | user.getName().isEmpty() | user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void checkUser(int userId, int friendId) {
        storage.getUser(userId);
        storage.getUser(friendId);
    }
}