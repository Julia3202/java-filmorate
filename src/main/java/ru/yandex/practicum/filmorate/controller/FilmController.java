package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> findAll() throws NotFoundException {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable("id") Integer id) throws NotFoundException, ValidationException {
        return filmService.findFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId)
            throws OtherException {
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFilm(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId)
            throws OtherException {
        filmService.removeLikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilm(@RequestParam(value = "count", defaultValue = "10") Integer count) {
        return filmService.findPopularFilm(count);
    }
    @GetMapping("/common")
    public List<Film> findCommonFilm(@RequestParam("userId") long userId,
                                     @RequestParam("friendId") long friendId) {
        //log.info("Найти список общих фильмов у пользователей с id --> {} и с id --> {}.", userId, friendId);
        return filmService.findCommonFilm(userId, friendId);
    }
}
