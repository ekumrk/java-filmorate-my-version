package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public User getUser(int id);

    List<User> getUsers();

    public User create(User user);

    public User update(User data);

    String delete(int id);

    void addFriend(int userID, int friendId);

    void removeFriend(int userID, int friendId);

    List<User> getFriendsById(int userId);

    List<User> getCommonFriends(int friend1, int friend2);

    boolean isFriend(int userId, int friendId);
}
