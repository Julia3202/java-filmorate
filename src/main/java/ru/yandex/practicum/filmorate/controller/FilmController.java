package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private static int id = 0;
    private final FilmValidation validation = new FilmValidation();
    private final Map<Integer, Film> films = new HashMap<>();

    public static int generateId() {
        return ++id;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if ((validation.validName(film) && (validation.validDescription(film)) &&
                (validation.validReleaseDate(film)) && (validation.validDuration(film)))) {
            id = generateId();
            film.setId(id);
            films.put(id, film);
            log.debug("Фильм {} был добавлен.", film.getName());
            return film;
        } else {
            throw new ValidationException("exception");
        }
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Данный фильм не был добавлен.");
        } else if ((validation.validName(film) && (validation.validDescription(film)) &&
                (validation.validReleaseDate(film)) && (validation.validDuration(film)))) {
            log.debug("Информация о фильме {} была изменена.", film.getName());
            films.put(id, film);
            return film;
        } else {
            throw new ValidationException("exception");
        }
    }

    @GetMapping("/films")
    public Collection<Film> findAll() throws ValidationException {
        if (films.isEmpty()) {
            throw new ValidationException("Список фильмов пока пуст.");
        } else {
            log.debug("Текущее количество фильмов: {}.", films.size());
            return films.values();
        }
    }
}