package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
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

    public Map<Integer, User> getUsers() throws NotFoundException {
        if (userStorage.getUsers().isEmpty()) {
            throw new NotFoundException("Список пользователей пуст.");
        } else {
            return userStorage.getUsers();
        }
    }

    public List<Integer> addFriends(Integer friendId, Integer id) throws OtherException, NotFoundException {
        if (!(getUsers().containsKey(id))) {
            throw new NotFoundException("Пользователь не найден.");
        }
        User userNow = getUsers().get(id);
        List<Integer> friend = userNow.getFriends();
        if (friend.contains(friendId)) {
            throw new OtherException("Пользователь не может быть добавлен повторно.");
        }
        userNow.getFriends().add(friendId);
        userNow.setFriends(friend);
        return friend;
    }

    public List<Integer> removeFriends(Integer friendId, Integer id) throws NotFoundException {
        if ((getUsers().containsKey(id))) {
            throw new NotFoundException("Пользователь не найден.");
        }
        User userNow = getUsers().get(id);
        List<Integer> friend = userNow.getFriends();
        if (!(friend.contains(friendId))) {
            throw new NotFoundException("Данного пользователя нет в списках ваших друзей.");
        }
        friend.remove(friendId);
        userNow.setFriends(friend);
        return friend;
    }

    public List<User> getAllFriends(int id) throws NotFoundException {
        if ((getUsers().containsKey(id))) {
            throw new NotFoundException("Пользователь не найден.");
        }
        User userNow = getUsers().get(id);
        Map<Integer, User> friend = new HashMap<>();
        if(userNow.getFriends() == null){
            return new ArrayList<>();
        }
        for (Integer idFriend : userNow.getFriends()) {
            friend.put(idFriend, getUsers().get(idFriend));
        }
        return new ArrayList<>(friend.values());
    }

    public List<User> getMutualFriend(int id, Integer otherId) throws NotFoundException {
        if (!(getUsers().containsKey(id)) || !(getUsers().containsKey(otherId))) {
            throw new NotFoundException("Пользователь не найден.");
        }
        List<User> mutualFriend = new ArrayList<>();
        User userNow = getUsers().get(id);
        User userFriend = getUsers().get(otherId);
        List<Integer> friend = userFriend.getFriends();
        for (Integer idFriend : userNow.getFriends()) {
            if (friend.contains(idFriend)) {
                mutualFriend.add(getUsers().get(idFriend));
            }
        }
        if(mutualFriend.isEmpty()){
            return new ArrayList<>();
        }
        return mutualFriend;
    }
}
