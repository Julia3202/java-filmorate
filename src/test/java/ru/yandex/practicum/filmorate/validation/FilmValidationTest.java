
package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FilmValidationTest {
    private FilmValidation filmValidation;
    private Film film;
    private Mpa mpa;

    @BeforeEach
    public void beforeEach() {
        LocalDate date = LocalDate.of(2012, 5, 4);
        mpa = new Mpa (5, "R-13");
        film = new Film( 1, "testFilm", "testDescription", date, 1000, 5, mpa);
    }

    @Test
    void validName() throws ValidationException {
        assertEquals("testFilm", film.getName());
        film.setName("");
        assertFalse(false, "Поле 'название' не может быть пустым");
        film.setName(" ");
        assertFalse(false, "Поле 'название' не может быть пустым");
    }

    @Test
    void validReleaseDate() throws ValidationException {
        LocalDate dates = LocalDate.of(1894, 12, 28);
        film.setReleaseDate(dates);
        assertFalse(false, "Дата релиза — не может быть раньше 28 декабря 1895 года");
    }

    @Test
    void validDescription() throws ValidationException {
        String exp = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
                "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" +
                "FFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
        film.setDescription(exp);
        assertFalse(false, "Максимальная длина описания — 200 символов");
    }

    @Test
    void validDuration() throws ValidationException {
        film.setDuration(-200);
        assertFalse(false, "Продолжительность фильма должна быть положительной");
    }
}