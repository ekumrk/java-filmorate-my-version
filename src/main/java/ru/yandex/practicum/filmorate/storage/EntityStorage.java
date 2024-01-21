package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.storage.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.ModelEntity;

import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class EntityStorage<T extends ModelEntity> {
    protected Map<Integer, T> storage;
    @PositiveOrZero(message = "Id cannot be a negative number")
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
        if (!(storage.containsKey(data.getId()))) {
            throw new DataNotFoundException(String.format("Объект с id %s отсутствует в хранилище.", data.getId()));
        }
        storage.put(data.getId(), data);
        log.debug(String.format("Объект %s успешно обновлён.", data));
        return data;
    }

    public abstract void validate(T data);
}
