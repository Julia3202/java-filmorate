package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final UserValidation validation = new UserValidation();
    private static int id = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<String, User> userEmail = new HashMap<>();

    public static int generateId() {
        return ++id;
    }

    @Override
    public User create(User user) throws ValidationException {
        if (userEmail.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь был зарегистрирован раньше.");
        }
        if (validation.validate(user)) {
            user.setId(generateId());
            log.debug("Пользователь {} добавлен", user.getName());
            userEmail.put(user.getEmail(), user);
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Пользователь не был добавлен.Ошибка валидации.");
        }
    }

    @Override
    public User update(User user) throws ValidationException, NotFoundException {
        User exsist = users.get(user.getId());
        User exsistByEmail = userEmail.get(user.getEmail());
        if (exsistByEmail != null && exsistByEmail != exsist) {
            throw new NotFoundException("Пользователь не был зарегистрирован.");
        } else if (validation.validate(user)) {
            log.debug("Информация о пользователе {} была изменена.", user.getName());
            userEmail.remove(exsist.getEmail());
            userEmail.put(user.getEmail(), user);
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Информация о пользователе не была изменена, ошибка валидации.");
        }
    }

    @Override
    public List<User> findAll() throws NotFoundException {
        if (users.isEmpty()) {
            throw new NotFoundException("Текущее количество пользователей: 0");
        } else {
            log.debug("Текущее количество пользователей: {}", users.size());
            return new ArrayList<>(users.values());
        }
    }

    @Override
    public User findUserById(Integer id) throws NotFoundException, ValidationException {
        if (users.isEmpty()) {
            throw new ValidationException("Текущее количество пользователей: 0");
        }
        if (users.get(id) == null) {
            throw new NotFoundException("Пользователь с таким ID не найден.");
        }
        return users.get(id);
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }
}
