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
    UserValidation validation = new UserValidation();
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<String, User> userEmail = new HashMap<>();

    public int generateId() {
        return ++id;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (userEmail.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь был зарегистрирован раньше.");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if ((validation.validLogin(user)) && (validation.validName(user))
                && (validation.validEmail(user)) && (validation.validBirthday(user))) {
            id = generateId();
            user.setId(id);
            log.debug("Пользователь {} добавлен", user.getName());
            userEmail.put(user.getEmail(), user);
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("exception");
        }
    }


    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) throws ValidationException {
        User exsist = users.get(user.getId());
        User exsistByEmail = userEmail.get(user.getEmail());
        if (exsistByEmail != null && exsistByEmail != exsist) {
            throw new ValidationException("Пользователь не был зарегистрирован.");
        } else if ((validation.validLogin(user)) && (validation.validEmail(user))
                && (validation.validBirthday(user)) && (validation.validName(user))) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            log.debug("Информация о пользователе {} была изменена.", user.getName());
            userEmail.remove(exsist.getEmail());
            userEmail.put(user.getEmail(), user);
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Ошибка валидации");
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
