package ru.yandex.practicum.filmorate.storage.daoimpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Repository
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "SELECT * FROM USERS";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        List<User> users = new ArrayList<>();
        while (rowSet.next()) {
            users.add(userMap(rowSet));
        }
        return users;
    }

    @Override
    public User create(User user) {
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("users")
                .usingColumns("login", "name", "email", "birthday")
                .usingGeneratedKeyColumns("user_id")
                .executeAndReturnKeyHolder(Map.of(
                        "login", user.getLogin(),
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "birthday", java.sql.Date.valueOf(user.getBirthday())))
                .getKeys();
        if (keys != null) {
            user.setId((Integer) keys.get("user_id"));
        }
        return user;
    }

    @Override
    public User update(User user) {
        getUser(user.getId());
        String sqlQuery = "UPDATE USERS "
                + "SET NAME = ?, "
                + "LOGIN = ?, "
                + "EMAIL = ?, "
                + "BIRTHDAY = ? "
                + "WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(),
                user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public String delete(int userId) {
        return "DELETE FROM USERS WHERE USER_ID = " + userId;
    }

    @Override
    public User getUser(int id) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (rowSet.next()) {
            return userMap(rowSet);
        } else {
            throw new DataNotFoundException("Пользователь с Id =" + id + " не найден!");
        }
    }

    public void addFriend(int userId, int friendId) {
        String sqlQuery = "MERGE into FRIENDS KEY(USER_ID, FRIEND_ID) VALUES(?, ?, true)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public List<User> getFriendsById(int userId) {
        List<User> friends = new ArrayList<>();
        String sqlQuery = "SELECT * FROM USERS "
                + "WHERE USERS.USER_ID IN (SELECT FRIEND_ID FROM FRIENDS "
                + "WHERE USER_ID = ?)";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        while (rowSet.next()) {
            friends.add(UserDbStorage.userMap(rowSet));
        }
        return friends;
    }

    public List<User> getCommonFriends(int friend1, int friend2) {
        List<User> commonFriends = new ArrayList<>();
        String sqlQuery = "SELECT * FROM USERS "
                + "WHERE USERS.USER_ID IN (SELECT FRIEND_ID FROM FRIENDS "
                + "WHERE USER_ID IN (?, ?) "
                + "AND FRIENDS.FRIEND_ID NOT IN (?, ?))";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, friend1, friend2, friend1, friend2);
        while (rowSet.next()) {
            commonFriends.add(UserDbStorage.userMap(rowSet));
        }
        return commonFriends;
    }

    public boolean isFriend(int userId, int friendId) {
        String sqlQuery = "SELECT * FROM FRIENDS WHERE "
                + "USER_ID = ? AND FRIEND_ID = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId, friendId);
        return rowSet.next();
    }

    private static User userMap(SqlRowSet rowSet) {
        return User.builder()
                .id(rowSet.getInt("user_id"))
                .login(rowSet.getString("login"))
                .name(rowSet.getString("name"))
                .email(rowSet.getString("email"))
                .birthday(Objects.requireNonNull(rowSet.getTimestamp("birthday"))
                        .toLocalDateTime().toLocalDate())
                .build();
    }
}