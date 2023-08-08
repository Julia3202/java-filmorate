package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDbStorage {

    Genre findGenreByIds(Integer id);

    List<Genre> findAllGenre();

    List<Genre> findGenreByFilmId(Integer id);

    void deleteGenreOfFilm(Integer id);

    void addGenreForFilm(Integer idGenre, Integer idFilm);
}