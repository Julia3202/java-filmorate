package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        return userService.update(user);
    }

    @GetMapping
    public Collection<User> findAll() throws NotFoundException {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable("id") Integer id)
            throws ClassNotFoundException, ValidationException {
        return userService.findUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId)
            throws OtherException {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriends(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId)
            throws NotFoundException {
        userService.removeFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable("id") Integer id) throws NotFoundException {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriend(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId)
            throws NotFoundException {
        return userService.getMutualFriend(id, otherId);
    }
}