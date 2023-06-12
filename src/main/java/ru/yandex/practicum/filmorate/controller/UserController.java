package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    UserValidation validation = new UserValidation();
    private int id = 0;
    private final Map<String, User> users = new HashMap<>();

    public int generateId() {
        return ++id;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
      /*  if (users.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь был зарегистрирован раньше.");
        } else */if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if ((validation.validLogin(user)) && (validation.validName(user))
                && (validation.validEmail(user)) && (validation.validBirthday(user))) {
            id = generateId();
            user.setId(id);
            log.debug("Пользователь {} добавлен", user.getName());
            users.put(user.getEmail(), user);
            return user;
        } else {
            throw new ValidationException("exception");
        }
    }


    @PutMapping("/users")
    public Map<String, User>  update(@Valid @RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь не был зарегистрирован.");
        } else if ((validation.validLogin(user)) && (validation.validEmail(user))
                && (validation.validBirthday(user)) && (validation.validName(user))) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            log.debug("Информация о пользователе {} была изменена.", user.getName());
            users.put(user.getEmail(), user);
            return users;
        } else {
            throw new ValidationException("Ошибка валидации");
        }
    }


    @GetMapping("/users")
    public Map<String, User> findAll() throws ValidationException {
        if (users.isEmpty()) {
            throw new ValidationException("Текущее количество пользователей: 0");
        } else {
         /*   Map<Integer, User> userArray = new HashMap<>();
            for(User user : users.values()){
                userArray.put(user.getId(), user);
            }*/
            log.debug("Текущее количество пользователей: {}", users.size());
            return users;
        }
    }

}
