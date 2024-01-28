package ru.yandex.practicum.filmorate.service.supportive;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.supportive.MPA;
import ru.yandex.practicum.filmorate.storage.daoimpl.MpaDbStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public MPA getRatingMpaById(int id) {
        MPA mpa = mpaDbStorage.getRatingMpaById(id);
        if (mpa == null) {
            throw new DataNotFoundException("Rating not found");
        }
        return mpa;
    }

    public List<MPA> getRatingsMpa() {
        return mpaDbStorage.getRatingsMpa();
    }
}
