package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.EntityStorage;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage extends EntityStorage<User> implements UserStorage {

    @Autowired
    public InMemoryUserStorage() {
        this.storage = new HashMap<>();
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return super.create(user);
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

    @Override
    public User getUser(int id) {
        return storage.get(id);
    }

    @Override
    public Map<Integer, User> getUsers() {
        return storage;
    }
}
