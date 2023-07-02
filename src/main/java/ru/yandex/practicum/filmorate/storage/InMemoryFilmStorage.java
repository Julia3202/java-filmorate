package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static int id = 0;
    private final FilmValidation validation = new FilmValidation();
    private final Map<Integer, Film> films = new HashMap<>();

    static int generateId() {
        return ++id;
    }

    @Override
    public Film create(Film film) throws ValidationException {
        if (validation.validate(film)) {
            film.setId(generateId());
            films.put(film.getId(), film);
            log.debug("Фильм {} был добавлен.", film.getName());
            return film;
        } else {
            throw new ValidationException("Фильм не был добавлен.Ошибка валидации.");
        }
    }

    @Override
    public Film update(Film film) throws NotFoundException, ValidationException {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Данный фильм не был найден.");
        } else if (validation.validate(film)) {
            log.debug("Информация о фильме {} была изменена.", film.getName());
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Информация о фильме не была изменена, ошибка валидации.");
        }
    }

    @Override
    public List<Film> findAll() throws NotFoundException {
        log.debug("Текущее количество фильмов: {}.", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(Integer id) throws NotFoundException {
        if (films.isEmpty()) {
            throw new NotFoundException("Текущее количество фильмов: 0");
        }
        if (films.get(id) == null) {
            throw new NotFoundException("Фильм с таким ID не найден.");
        }
        return films.get(id);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }
}
