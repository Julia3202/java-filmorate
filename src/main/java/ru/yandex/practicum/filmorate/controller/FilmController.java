package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        return inMemoryFilmStorage.update(film);
    }

    @GetMapping("/films")
    public List<Film> findAll() throws NotFoundException {
        return inMemoryFilmStorage.findAll();
    }

    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable("id") Integer id) throws ClassNotFoundException, ValidationException {
        return inMemoryFilmStorage.findFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public List<Integer> likeFilm(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId)
            throws OtherException {
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeFilm(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId)
            throws OtherException {
        filmService.removeFilm(id, userId);
    }

    @GetMapping("/films/popular?count={count}")
    public List<Film> findListFirstFilm(@PathVariable("count") Integer count) {
        return filmService.findListFirstFilm(count);
    }


}