package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
public class User extends ModelEntity {
    @NotBlank
    @NotNull
    private final String login;

    private String name;

    @NotBlank
    @NotNull
    @Email
    private final String email;

    @PastOrPresent
    private final LocalDate birthday;
    @JsonIgnore
    private Set<Integer> idFriends = new HashSet<>();

    public void addFriend(int idUser) {
        idFriends.add(idUser);
    }

    public void removeFriend(int idUser) {
        idFriends.remove(idUser);
    }
}
