package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
public class DaoController {
    private final FilmService filmService;

    @Autowired
    public DaoController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/genres")
    public List<Genre> findAllGenre() {
        return filmService.findAllGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre findGenreByIds(@PathVariable("id") Integer id) {
        return filmService.findGenreByIds(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMPA() {
        return filmService.getAllMpa();
    }

    @GetMapping("mpa/{id}")
    public Mpa getMpaById(@PathVariable("id") Integer id) {
        return filmService.getMpaById(id);
    }
}
