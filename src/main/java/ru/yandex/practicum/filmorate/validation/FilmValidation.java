package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidation {
    private final LocalDate DATE_OF_FIRST_FILM = LocalDate.of(1895, 12, 28);

    public boolean validName(Film film) throws ValidationException {
        if ((film.getName() == null) || (film.getName().isBlank())) {
            log.warn("Поле 'название' не может быть пустым");
            throw new ValidationException("Поле 'название' не может быть пустым");
        } else {
            return true;
        }
    }

    public boolean validReleaseDate(Film film) throws ValidationException {
        if (film.getReleaseDate() == null || (film.getReleaseDate().toString().isBlank())) {
            log.warn("Нет даты релиза.");
            throw new ValidationException("Необходимо указать дату релиза.");

        }
        if (film.getReleaseDate().isBefore(DATE_OF_FIRST_FILM)) {
            log.warn("Дата релиза — не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не может быть раньше 28 декабря 1895 года");
        } else {
            return true;
        }
    }

    public boolean validDescription(Film film) throws ValidationException {
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.warn("Нет описания фильма.");
            throw new ValidationException("Необходимо указать описание фильма.");
        } else if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else {
            return true;
        }
    }

    public boolean validDuration(Film film) throws ValidationException {
        if ((String.valueOf(film.getDuration()) == null) || (film.getDuration() <= 0)) {
            log.warn("Указана неверная продолжительность фильма.");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        } else {
            return true;
        }
    }
}
