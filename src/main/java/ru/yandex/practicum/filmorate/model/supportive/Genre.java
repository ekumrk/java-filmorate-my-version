package ru.yandex.practicum.filmorate.model.supportive;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class Genre extends ModelEntity {

    String name;

    public Genre(int id, String name) {
        super(id);
        this.name = name;
    }
}
