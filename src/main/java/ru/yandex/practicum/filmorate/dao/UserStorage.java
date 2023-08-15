package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    User update(User user);

    List<User> findAll();

    Optional<User> findUserById(Integer id);

    void addFriends(Integer id, Integer friendId);

    void removeFriends(Integer id, Integer friendId);

    List<User> getMutualFriend(Integer id, Integer otherId);

    List<User> getAllFriends(Integer id);
}
