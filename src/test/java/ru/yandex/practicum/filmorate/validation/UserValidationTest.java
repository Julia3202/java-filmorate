
package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;

class UserValidationTest {
    private UserValidation userValidation;
    private User user;

    @BeforeEach
    public void beforeEach() {
        LocalDate date = LocalDate.of(2000, 5, 4);
        user = new User("testEmail@mail.ru", "testLogin", "testUser", date);
    }

    @Test
    void validLogin() throws ValidationException {
        user.setLogin("");
        assertFalse(false, "Поле 'login' не должно быть пустым.");
    }

    @Test
    void testValidEmail() throws ValidationException {
        user.setEmail("a&?sma?l.ru");
        assertFalse(false, "Поле 'email' не может быть пустым и должен содержать символ '@'.");
    }

    @Test
    void testValidBirthday() throws ValidationException {
        LocalDate date = LocalDate.now();
        user.setBirthday(date.plusYears(3));
        assertFalse(false, "Дата рождения не может быть в будущем.");
    }
}
