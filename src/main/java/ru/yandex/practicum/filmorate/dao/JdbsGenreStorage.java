package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

@Component
public class JdbsGenreStorage implements GenreDbStorage {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Autowired
    public JdbsGenreStorage(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Genre findGenreByIds(Integer id) {
        String sqlQuery = "select GENRE_ID, GENRE_NAME from GENRE where GENRE_ID = :id";
        final List<Genre> genres = jdbcOperations.query(sqlQuery, Map.of("id", id), new GenreRowMapper());
        if (genres.size() != 1) {
            throw new NotFoundException("Genre with id {} " + id + " not found.");
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> findAllGenre() {
        String sqlQuery = "select GENRE_ID, GENRE_NAME from GENRE";
        return jdbcOperations.query(sqlQuery, new GenreRowMapper());
    }

    @Override
    public List<Film> findFilmByIdGenre(Integer id) {
        String sqlQuery = "select FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID, GENRES_ID" +
                " from FILMS where GENRES_ID = :id";
        List<Film> films = jdbcOperations.query(sqlQuery, Map.of("id", id), new FilmRowMapper());
        return films;
    }
}