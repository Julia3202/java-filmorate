package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
public class JdbcFilmStorage implements FilmDbStorage {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final JdbcTemplate jdbcTemplate;
    private final FilmValidation validation = new FilmValidation();

    public JdbcFilmStorage(NamedParameterJdbcOperations jdbcOperations, JdbcTemplate jdbcTemplate) {
        this.jdbcOperations = jdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
    }

    private void setGenreToFilm(Film film) {
        if (film.getGenre() != null && !film.getGenre().isEmpty()) {
            for(Genre genres: film.getGenre()){
                String sqlQuery = "insert into GENRE (FILM_ID, GENRE_ID) values(?, ?)";
                jdbcTemplate.update(sqlQuery, film.getId(), genres.getId());
            }
        }
    }

    @Override
    public Film create(Film film) throws ValidationException {
        String sqlQuery = "insert into FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)" +
                " values(:name, :description, :release_date, :duration, :mpa_id)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        if (validation.validate(film)) {
            map.addValue("name", film.getName());
            map.addValue("description", film.getDescription());
            map.addValue("release_date", film.getReleaseDate());
            map.addValue("duration", film.getDuration());
            map.addValue( "mpa_id", film.getMpa().getId());
            jdbcOperations.update(sqlQuery, map, keyHolder);
            film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        }
        setGenreToFilm(film);
        return film;
    }

    @Override
    public Film update(Film film) throws NotFoundException, ValidationException {
        if (findFilmById(film.getId()) == null) {
            throw new NotFoundException("Данный фильм не был найден.");
        }
        String sqlQuery = "update FILMS set FILM_NAME=:name, FILM_DESCRIPTION=:description, RELEASE_DATE=:release_date" +
                ", DURATION=:duration, MPA_ID=:mpa_id where FILM_ID=:id";
        MapSqlParameterSource map = new MapSqlParameterSource();
        if (validation.validate(film)) {
            map.addValue("id", film.getId());
            map.addValue("name", film.getName());
            map.addValue("description", film.getDescription());
            map.addValue("release_date", film.getReleaseDate());
            map.addValue("duration", film.getDuration());
            map.addValue("mpa_id", film.getMpa().getId());
            jdbcOperations.update(sqlQuery, map);
        }
        setGenreToFilm(film);
        return film;
    }

    @Override
    public List<Film> findAll() throws NotFoundException {
        String sqlQuery =
                "select FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID, GENRES_ID" +
                        " from FILMS";
        List<Film> filmList = jdbcOperations.query(sqlQuery, this::mapRow);
        return filmList;
    }

    @Override
    public Film findFilmById(Integer id) throws NotFoundException {
        String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.GENRES_ID," +
                " f.MPA_ID" +
                " from FILMS f " +
                " where f.FILM_ID = :filmId";
        final List<Film> filmList = jdbcOperations.query(sqlQuery, Map.of("filmId", id), new FilmRowMapper());
        if (filmList.size() != 1) {
            throw new NotFoundException("Фильм с таким ID не найден.");
        }
        return filmList.get(0);
    }

    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("FILM_DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION")
        );
    }
}