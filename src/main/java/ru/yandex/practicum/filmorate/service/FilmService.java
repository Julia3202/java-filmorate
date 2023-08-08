package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.LikeFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final LikeFilmStorage likeFilmStorage;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findFilmById(Integer id) {
        return filmStorage.findFilmById(id);
    }

    public void likeFilm(Integer id, Integer userId) {
        likeFilmStorage.likeFilm(id, userId);
    }

    public void removeLikeFilm(Integer id, Integer userId) {
        likeFilmStorage.removeLikeFilm(id, userId);
    }

    public List<Film> findPopularFilm(Integer count) {
        return likeFilmStorage.findPopularFilm(count);
    }
}
