package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends ModelEntity {
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @JsonIgnore
    private Set<Integer> idFriends = new HashSet<>();

    public void addFriend(int idUser) {
        idFriends.add(idUser);
    }

    public void removeFriend(int idUser) {
        idFriends.remove(idUser);
    }
}
