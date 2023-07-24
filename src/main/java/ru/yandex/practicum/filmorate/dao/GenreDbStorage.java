package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDbStorage {

    Genre findGenreByIds(Integer id);
    List<Genre> findAllGenre();

    //List<Genre> findGenreFromFilm(Integer id);
//
//    void setsGenre(List<Film> films);
//    void loadsGenre(List<Film> film);
    List<Film> findFilmByIdGenre(Integer id);
}
