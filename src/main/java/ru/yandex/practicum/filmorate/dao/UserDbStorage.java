package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDbStorage {
    User create(User user);

    User update(User user);

    List<User> findAll();

    User findUserById(Integer id);
}
