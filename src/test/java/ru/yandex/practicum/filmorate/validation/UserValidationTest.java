package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidationTest {
    private final UserController controller = new UserController();


    @Test
    void validLogin() throws ValidationException {
        LocalDate date = LocalDate.now();
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User(1, "as@mail.ru", " ", "name", date);
                    controller.create(user);
                });
        assertEquals("Поле 'login' не должно быть пустым.", exseption.getMessage());
        User users = new User(1, "as2@mail.ru", "login2", "name2", date);
        User result = controller.create(users);
        assertEquals(users, result, "Test Name with exception is done.");
    }

    @Test
    void testValidEmail() throws ValidationException {
        LocalDate date = LocalDate.now();
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User(0, "a&?sma?l.ru", "login", "name", date);
                    controller.create(user);
                });
        assertEquals("Поле 'email' не может быть пустым и должен содержать символ '@'.", exseption.getMessage());
        User users = new User(1, "as@mail.ru", "login", " ", date);
        User result = controller.create(users);
        assertEquals(users, result, "Test Email with exception is done.");
    }

    @Test
    void testValidBirthday() throws ValidationException {
        LocalDate date = LocalDate.now();
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User(0, "as@mail.ru", "login", "name", date.plusYears(3));
                    controller.create(user);
                });
        assertEquals("Дата рождения не может быть в будущем.", exseption.getMessage());
        User users = new User(1, "as@mail.ru", "login", "", date.minusYears(20));
        User result = controller.create(users);
        assertEquals(users, result, "Test Birthday with exception is done.");
    }
}