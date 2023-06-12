package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidationTest {
    UserValidation validation = new UserValidation();
    UserController controller = new UserController();


    @Test
    void validLogin() throws ValidationException {
        LocalDate date = LocalDate.now();
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User("as@mail.ru", " ", "name", date);
                    controller.create(user);
                });
        assertEquals("Поле 'login' не должно быть пустым.", exseption.getMessage());
        User user2 = new User("as2@mail.ru", "login2", "name2", date);
        User result = controller.create(user2);
        assertEquals(user2, result, "Test Name with exception is done.");
        User user3 = new User("as3@mail.ru", "login3","", date.minusYears(20));
        User result2 = controller.create(user3);
        Map<String, User> userList = controller.findAll();
        System.out.println(userList);
        User result4 = controller.create(new User("mail@mail.ru","dolore", "Nick Name", LocalDate.of(1946, 8, 20)));
        System.out.println(result4);


    }

    @Test
    void testValidEmail() throws ValidationException {
        LocalDate date = LocalDate.now();
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User("a&?sma?l.ru", "login","name", date);
                    controller.create(user);
                });
        assertEquals("Поле 'email' не может быть пустым и должен содержать символ '@'.", exseption.getMessage());
        User user2 = new User("as@mail.ru", "login"," ", date);
        User result = controller.create(user2);
        User user3 = new User("as3@mail.ru", "login"," ", date);
        Map<String, User> result2 = controller.update(result);
        assertEquals(user2, result, "Test Email with exception is done.");
    }

    @Test
    void testValidBirthday() throws ValidationException {
        LocalDate date = LocalDate.now();
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    User user = new User("as@mail.ru", "login","name", date.plusYears(3));
                    controller.create(user);
                });
        assertEquals("Дата рождения не может быть в будущем.", exseption.getMessage());
        User user2 = new User("as@mail.ru", "login", "", date.minusYears(20));
        User result = controller.create(user2);
        assertEquals(user2, result, "Test Birthday with exception is done.");
    }

}