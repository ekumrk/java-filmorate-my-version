package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Film extends ModelEntity {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @JsonIgnore
    private Set<Integer> usersWhoLikeIt = new HashSet<>();

    @Setter(AccessLevel.NONE)
    private int likes = 0;

    @SneakyThrows
    public void addLike(int idUser) {
        try {
            usersWhoLikeIt.add(idUser);
            likes++;
        } catch (Exception e) {
            throw new ValidationException("Вы уже лайкали этот фильм");
        }
    }

    @SneakyThrows
    public void removeLike(int idUser) {
        if (usersWhoLikeIt.contains(idUser)) {
            usersWhoLikeIt.remove(idUser);
            likes--;
        }
    }
}
