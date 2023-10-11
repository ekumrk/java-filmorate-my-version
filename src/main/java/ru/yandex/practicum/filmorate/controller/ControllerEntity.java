package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.controller.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.ModelEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public abstract class ControllerEntity<T extends ModelEntity> {
    private final Map<Integer, T> storage = new HashMap<>();
    private int currentId = 1;

    public List<T> getStorage() {
        return new ArrayList<>(storage.values());
    }

    public T create(T data) {
        validate(data);
        data.setId(currentId);
        storage.put(currentId, data);
        log.debug(String.format("Объект %s успешно добавлен.", data));
        currentId++;
        return data;
    }

    public T update(T data) {
        if(!(storage.containsKey(data.getId()))) {
            throw new DataNotFoundException(String.format("Объект %s таким %s отсутствует в хранилище.", data,
                    data.getId()));
        }
        storage.put(data.getId(), data);
        log.debug(String.format("Объект %s успешно обновлён.", data));
        return data;
    }

    public abstract void validate(T data);
}
