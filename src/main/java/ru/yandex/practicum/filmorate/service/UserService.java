package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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
        return userStorage.findUserById(id);
    }

    public Map<Integer, User> getUsers() throws NotFoundException {
        if (userStorage.getUsers().isEmpty()) {
            throw new NotFoundException("Список пользователей пуст.");
        }
        return userStorage.getUsers();
    }

    public void addFriends(Integer friendId, Integer id) throws OtherException, NotFoundException {
        if ((!(getUsers().containsKey(id))) || (!(getUsers().containsKey(friendId)))) {
            throw new NotFoundException("Пользователь не найден.");
        }
        User userNow = getUsers().get(id);
        List<Integer> friend = (userNow.getFriends() == null) ? new ArrayList<>() : userNow.getFriends();

        if (friend.contains(friendId)) {
            throw new OtherException("Пользователь не может быть добавлен повторно.");
        }
        friend.add(friendId);
        User userFriend = getUsers().get(friendId);
        List<Integer> friendList = (userNow.getFriends() == null) ? new ArrayList<>() : userFriend.getFriends();
        friendList.add(id);
        userFriend.setFriends(friendList);
    }

    public void removeFriends(Integer friendId, Integer id) throws NotFoundException {
        if ((!(getUsers().containsKey(id))) || (!(getUsers().containsKey(friendId)))) {
            throw new NotFoundException("Пользователь не найден.");
        }
        User userNow = getUsers().get(id);
        List<Integer> friend;
        if (userNow.getFriends() == null) {
            log.info("Список друзей пуст.");
        } else {
            friend = userNow.getFriends();
            friend.remove(friendId);
        }
        User userFriend = getUsers().get(friendId);
        List<Integer> friendList;
        if (userFriend.getFriends() == null) {
            log.info("Список друзей пуст.");
        } else {
            friendList = userFriend.getFriends();
            friendList.remove(id);
        }
    }

    public List<User> getAllFriends(Integer id) throws NotFoundException {
        if (!(getUsers().containsKey(id))) {
            throw new NotFoundException("Пользователь не найден.");
        }
        User userNow = getUsers().get(id);
        List<User> friend = new ArrayList<>();
        if (userNow.getFriends().isEmpty()) {
            throw new NotFoundException("Список друзей пуст.");
        }
        for (Integer idFriend : userNow.getFriends()) {
            friend.add(getUsers().get(idFriend));
        }
        return friend;
    }

    public List<User> getMutualFriend(Integer id, Integer otherId) throws NotFoundException {
        if (!(getUsers().containsKey(id)) || !(getUsers().containsKey(otherId))) {
            throw new NotFoundException("Пользователь не найден.");
        }
        List<User> mutualFriend = new ArrayList<>();
        User userNow = getUsers().get(id);
        User userFriend = getUsers().get(otherId);
        if ((userNow.getFriends() == null) || (userFriend.getFriends() == null)) {
            log.info("У Вас нет друзей в списке.");
            return mutualFriend;
        }
        List<Integer> friend = userFriend.getFriends();
        for (Integer idFriend : userNow.getFriends()) {
            if (friend.contains(idFriend)) {
                mutualFriend.add(getUsers().get(idFriend));
            }
        }
        return mutualFriend;
    }
}