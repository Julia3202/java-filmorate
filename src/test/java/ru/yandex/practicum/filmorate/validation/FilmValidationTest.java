package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmValidationTest {
    private final FilmController controller = new FilmController();

    @Test
    void validName() throws ValidationException {
        LocalDate date = LocalDate.of(2012, 5, 4);
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = new Film(0, " ", "описание", date, 25);
                    controller.create(film);
                });
        assertEquals("Поле 'название' не может быть пустым", exseption.getMessage());
        Film films = new Film(1, "testName", "testDescription", date, 120);
        Film result = controller.create(films);
        assertEquals(films, result, "Test Name with exception is done.");
    }

    @Test
    void validReleaseDate() throws ValidationException {
        LocalDate dates = LocalDate.of(2021, 10, 5);
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    LocalDate date = LocalDate.of(1894, 12, 28);
                    Film film = new Film(0, "фильм", "описание", date, 25);
                    controller.create(film);
                });
        assertEquals("Дата релиза — не может быть раньше 28 декабря 1895 года", exseption.getMessage(),
                "Test Date with exception is done.");
        Film films = new Film(1, "testName", "testDescription", dates, 120);
        Film result = controller.create(films);
        Film results = controller.update(result);
        assertEquals(films, results, "Test Data is done.");
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
                    Film film = new Film(0, "фильм", exp, date, 25);
                    controller.create(film);
                });
        assertEquals("Максимальная длина описания — 200 символов", exseption.getMessage(),
                "Test Description with exception is done.");
        Film films = new Film(1, "testName", "testDescription", date, 120);
        Film result = controller.create(films);
        assertEquals(films, result, "Test Description is done.");
    }

    @Test
    void validDuration() throws ValidationException {
        LocalDate date = LocalDate.of(2012, 5, 4);
        ValidationException exseption = assertThrows(
                ValidationException.class,
                () -> {
                    Film film = new Film(0, "фильм", "описание", date, -200);
                    controller.create(film);
                });
        assertEquals("Продолжительность фильма должна быть положительной", exseption.getMessage());
        Film films = new Film(1, "testName", "testDescription", date, 120);
        Film result = controller.create(films);
        Film filmses = new Film(2, "testName2", "testDescription2", date, 121);
        Film result2 = controller.create(filmses);
        System.out.println(result + "\n" + result2);
        assertEquals(films, result, "Test Duration is done.");
        assertEquals("testName", result.getName());
        assertEquals("testDescription", result.getDescription());
        assertEquals(date, result.getReleaseDate());
        assertEquals(0, result.getId());
        assertEquals(120, result.getDuration());
    }
}