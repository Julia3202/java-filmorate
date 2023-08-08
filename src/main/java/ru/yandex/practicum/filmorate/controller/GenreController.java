package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> findAllGenre() {
        return genreService.findAllGenre();
    }

    @GetMapping("/{id}")
    public Genre findGenreByIds(@PathVariable("id") Integer id) {
        return genreService.findGenreByIds(id);
    }

    @GetMapping("/film/{id}")
    public List<Genre> findGenreByFilmId(@PathVariable int id) {
        return genreService.findGenreByFilmId(id);
    }
}
