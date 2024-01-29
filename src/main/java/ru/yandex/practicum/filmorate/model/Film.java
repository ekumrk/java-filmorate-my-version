package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.model.supportive.Genre;
import ru.yandex.practicum.filmorate.model.supportive.MPA;
import ru.yandex.practicum.filmorate.model.validation.AfterDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
public class Film {
    @PositiveOrZero(message = "ID не может быть отрицательным числом")
    private int id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @NotBlank
    @Length(max = 200, message = "Описание не может превышать 200 символов")
    private String description;

    @NotNull
    @AfterDate
    private LocalDate releaseDate;

    @NotNull(message = "Длительность фильма не может быть пустой")
    @Positive(message = "Длительность фильма не может быть отрицательной или нулем")
    private int duration;

    private Set<Genre> genres;

    private MPA mpa;

    @JsonIgnore
    private final Set<Integer> likes = new HashSet<>();

    public void addLike(int id) {
        likes.add(id);
    }

    public void deleteLike(int id) {
        likes.remove(id);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
}