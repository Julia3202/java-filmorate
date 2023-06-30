package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    User create(User user) throws ValidationException;

    User update(User user) throws ValidationException;

    List<User> findAll() throws ValidationException, NotFoundException;
    Map<Integer, User> getUsers();
    User findUserById(Integer id) throws NotFoundException, ValidationException;
}
