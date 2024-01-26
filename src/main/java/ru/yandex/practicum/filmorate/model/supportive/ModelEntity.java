package ru.yandex.practicum.filmorate.model.supportive;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.PositiveOrZero;

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public abstract class ModelEntity {
    @PositiveOrZero(message = "Идентификатор не может быть меньше нуля")
    public final int id;
}
