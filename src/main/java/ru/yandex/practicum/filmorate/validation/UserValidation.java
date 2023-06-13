package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidation {
    public boolean validName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return true;
    }

    public boolean validLogin(User user) throws ValidationException {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Поле 'login' не должно быть пустым.");
            throw new ValidationException("Поле 'login' не должно быть пустым.");
        } else {
            return true;
        }
    }

    public boolean validEmail(User user) throws ValidationException {
        if ((user.getEmail() == null) || (!user.getEmail().contains("@"))) {
            log.warn("Поле 'email' не может быть пустым и должен содержать символ '@'.");
            throw new ValidationException("Поле 'email' не может быть пустым и должен содержать символ '@'.");
        } else {
            return true;
        }
    }

    public boolean validBirthday(User user) throws ValidationException {
        if (String.valueOf(user.getBirthday()) == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        } else {
            return true;
        }
    }

}
