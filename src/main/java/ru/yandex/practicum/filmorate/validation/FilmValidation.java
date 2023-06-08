package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidation {
    public boolean validName(Film film) throws ValidationException {
        if ((film.getName().isBlank()) || film.getName() == null) {
            log.warn("Поле 'название' не может быть пустым");
            throw new ValidationException("Поле 'название' не может быть пустым");
        } else {
            return true;
        }
    }

    public boolean validReleaseDate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза — не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не может быть раньше 28 декабря 1895 года");
        } else if(film. getReleaseDate() == null){
            log.warn("Нет даты релиза.");
            throw new ValidationException("Необходимо указать дату релиза.");
        }else{
            return true;
        }
    }

    public boolean validDescription(Film film) throws ValidationException {
        if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if(film. getDescription() == null){
            log.warn("Нет даты релиза.");
            throw new ValidationException("Необходимо указать дату релиза.");
        } else {
            return true;
        }
    }

    public boolean validDuration(Film film) throws ValidationException {
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        } else {
            return true;
        }
    }

}
