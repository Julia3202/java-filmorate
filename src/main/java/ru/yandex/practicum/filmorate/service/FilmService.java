package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage, GenreDbStorage genreDbStorage, MpaDbStorage mpaDbStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

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

    public User findUserById(Integer id) {
        return userStorage.findUserById(id);
    }

    public Film likeFilm(Integer id, Integer userId) throws OtherException {
        if (findUserById(userId) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (findFilmById(id) == null) {
            throw new NotFoundException("Фильм не найден.");
        }
        SqlRowSet sqlQuery = jdbcTemplate.queryForRowSet("select user_id from USER_LIKE where FILM_ID = ?", id);
        if (sqlQuery.next()) {
            throw new OtherException("Нельзя оценивать фильм больше 1 раза.");
        }
        String query = ("insert into USER_LIKE(FILM_ID, USER_ID) value(?, ?)");
        jdbcTemplate.update(query, id, userId);

        return findFilmById(id);
    }

    public void removeLikeFilm(Integer id, Integer userId) throws OtherException {
        if (findUserById(userId) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (findFilmById(id) == null) {
            throw new NotFoundException("Фильм не найден.");
        }
        SqlRowSet sqlQuery = jdbcTemplate.queryForRowSet("select user_id from USER_LIKE where FILM_ID = ?", id);
        if (sqlQuery.next()) {
            String sqlQuerys = "delete from USER_LIKE where FILM_ID=? and USER_ID=?)";
            jdbcTemplate.update(sqlQuerys, id, userId);
        } else {
            throw new OtherException("Нельзя удалить оценку, если она не была поставлена.");
        }

    }

    public List<Film> findPopularFilm(Integer count) {
        String sqlQuery = ("SELECT f.film_id, f.film_name, f.film_description, f.release_date, f.duration , f.genres_id, f.mpa_id" +
                " FROM films f WHERE f.film_id in " +
                "(SELECT u.film_id FROM user_like u GROUP BY u.film_id ORDER BY COUNT(u.user_id) DESC ) LIMIT ?");
        return jdbcTemplate.query(sqlQuery, new FilmRowMapper(), count);
    }

    public Genre findGenreByIds(Integer id) {
        return genreDbStorage.findGenreByIds(id);
    }

    public List<Genre> findAllGenre() {
        return genreDbStorage.findAllGenre();
    }

    public List<Film> findFilmByIdGenre(Integer id) {
        return genreDbStorage.findFilmByIdGenre(id);
    }

    public List<Mpa> getAllMpa() {
        return mpaDbStorage.getAll();
    }

    public Mpa getMpaById(Integer id) {
        return mpaDbStorage.getBYId(id);
    }
}
