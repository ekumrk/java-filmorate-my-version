package ru.yandex.practicum.filmorate.model.supportive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MPA extends ModelEntity {

    String name;

     public MPA(int id, String name) {
         super(id);
         this.name = name;
     }

}
