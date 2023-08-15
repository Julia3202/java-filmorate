package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeFilmStorage {

    void likeFilm(Integer id, Integer userId);

    void removeLikeFilm(Integer id, Integer userId);

    List<Film> findPopularFilm(Integer count);

    List<Integer> findUserLike(Integer id, Integer userId);
}
