package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmValidationTest {
    FilmValidation validation = new FilmValidation();
    FilmController controller = new FilmController();

    @Test
    void validName() throws ValidationException {
        LocalDate date = LocalDate.of(2012, 5, 4);
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = new Film(" ", "описание", date, 25);
                    controller.create(film);
                });
        assertEquals("Поле 'название' не может быть пустым", exseption.getMessage());

        Film film2 = new Film("testName", "testDescription", date, 120);
        Film result = controller.create(film2);
        assertEquals(film2, result, "Test Name with exception is done.");

    }

    @Test
    void validReleaseDate() throws ValidationException {
        LocalDate date2 = LocalDate.of(2021, 10, 5);
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    LocalDate date = LocalDate.of(1894, 12, 28);
                    Film film = new Film("фильм", "описание", date, 25);
                    controller.create(film);
                });
        assertEquals("Дата релиза — не может быть раньше 28 декабря 1895 года", exseption.getMessage(),
                "Test Date with exception is done.");

        Film film2 = new Film("testName", "testDescription", date2, 120);
        Film result = controller.create(film2);
        Film result2 = controller.update(result);
        assertEquals(film2, result2, "Test Data is done.");


    }

    @Test
    void validDescription() throws ValidationException {
        LocalDate date = LocalDate.of(2021, 10, 5);
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    String exp = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
                            "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
                            "FFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
                    Film film = new Film("фильм", exp, date, 25);
                    controller.create(film);
                });
        assertEquals("Максимальная длина описания — 200 символов", exseption.getMessage(),
                "Test Description with exception is done.");

        Film film2 = new Film("testName", "testDescription", date, 120);
        Film result = controller.create(film2);
        assertEquals(film2, result, "Test Description is done.");
    }

    @Test
    void validDuration() throws ValidationException {
        LocalDate date = LocalDate.of(2012, 5, 4);
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = new Film("фильм", "описание", date, -200);
                    controller.create(film);
                });
        assertEquals("Продолжительность фильма должна быть положительной", exseption.getMessage());
        Film film2 = new Film("testName", "testDescription", date, 120);
        Film result = controller.create(film2);

        Film film3 = new Film("testName2", "testDescription2", date, 121);
        Film result2 = controller.create(film3);
        System.out.println(result + "\n" + result2);
        assertEquals(film2, result, "Test Duration is done.");
        assertEquals("testName", result.getName());
        assertEquals("testDescription", result.getDescription());
        assertEquals(date, result.getReleaseDate());
        assertEquals(0, result.getId());
        assertEquals(120, result.getDuration());

    }
}