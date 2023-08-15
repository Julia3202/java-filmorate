package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findUserById(Integer id) {
        return userStorage.findUserById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден."));
    }

    public void addFriends(Integer id, Integer friendId) {
        userStorage.addFriends(id, friendId);
    }

    public void removeFriends(Integer id, Integer friendId) {
        userStorage.removeFriends(id, friendId);
    }

    public List<User> getAllFriends(Integer id) {
        return userStorage.getAllFriends(id);
    }

    public List<User> getMutualFriend(Integer id, Integer otherId) {
        return userStorage.getMutualFriend(id, otherId);
    }
}
