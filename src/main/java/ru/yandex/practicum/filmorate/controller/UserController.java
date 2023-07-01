package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        return inMemoryUserStorage.create(user);
    }


    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) throws ValidationException {
        return inMemoryUserStorage.update(user);
    }

    @GetMapping("/users")
    public Collection<User> findAll() throws ClassNotFoundException {
        return inMemoryUserStorage.findAll();
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable("id") Integer id)
            throws ClassNotFoundException, ValidationException {
        return inMemoryUserStorage.findUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId)
            throws OtherException {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriends(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId)
            throws NotFoundException {
        userService.removeFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getAllFriends(@PathVariable("id") Integer id) throws NotFoundException {
        return userService.getAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriend(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId)
            throws NotFoundException {
        return userService.getMutualFriend(id, otherId);
    }
}
