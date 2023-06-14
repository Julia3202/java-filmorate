package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private final UserValidation validation = new UserValidation();
    private static int id = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<String, User> userEmail = new HashMap<>();

    public static int generateId() {
        return ++id;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (userEmail.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь был зарегистрирован раньше.");
        }
        if (validation.validation(user)) {
            user.setId(generateId());
            log.debug("Пользователь {} добавлен", user.getName());
            userEmail.put(user.getEmail(), user);
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Пользователь не был добавлен.Ошибка валидации.");
        }
    }


    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) throws ValidationException {
        User exsist = users.get(user.getId());
        User exsistByEmail = userEmail.get(user.getEmail());
        if (exsistByEmail != null && exsistByEmail != exsist) {
            throw new ValidationException("Пользователь не был зарегистрирован.");
        } else if (validation.validation(user)) {
            log.debug("Информация о пользователе {} была изменена.", user.getName());
            userEmail.remove(exsist.getEmail());
            userEmail.put(user.getEmail(), user);
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Информация о пользователе не была изменена, ошибка валидации.");
        }
    }


    @GetMapping("/users")
    public Collection<User> findAll() throws ValidationException {
        if (users.isEmpty()) {
            throw new ValidationException("Текущее количество пользователей: 0");
        } else {
            log.debug("Текущее количество пользователей: {}", users.size());
            return users.values();
        }
    }

}
