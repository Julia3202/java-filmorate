package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JdbcFilmStorage implements FilmDbStorage {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final MpaDbStorage mpaStorage;
    private final GenreDbStorage genreStorage;
    private final FilmValidation validation = new FilmValidation();

    @Override
    public Film create(Film film) throws ValidationException {
        String sqlQuery = "insert into FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID, LIKES)" +
                " values(:name, :description, :release_date, :duration, :mpa_id, :likes)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        if (validation.validate(film)) {
            map.addValue("name", film.getName());
            map.addValue("description", film.getDescription());
            map.addValue("release_date", film.getReleaseDate());
            map.addValue("duration", film.getDuration());
            map.addValue("mpa_id", film.getMpa().getId());
            map.addValue("likes", film.getLikes());
        }
        jdbcOperations.update(sqlQuery, map, keyHolder);
        Integer filmId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(filmId);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Genre> genreList = film.getGenres().stream()
                    .distinct()
                    .collect(Collectors.toList());
            for (Genre genre : genreList) {
                genreStorage.addGenreForFilm(genre.getId(), filmId);
            }
        }
        film.setGenres(genreStorage.findGenreByFilmId(filmId));
        return findFilmById(filmId);
    }

    @Override
    public Film update(Film film) throws NotFoundException, ValidationException {
        if (findFilmById(film.getId()) == null) {
            throw new NotFoundException("Данный фильм не был найден.");
        }
        int id = film.getId();
        String sqlQuery = "update FILMS set FILM_NAME=:name, FILM_DESCRIPTION=:description, RELEASE_DATE=:release_date" +
                ", DURATION=:duration, MPA_ID=:mpa_id, LIKES=:likes where FILM_ID=:id";
        MapSqlParameterSource map = new MapSqlParameterSource();
        if (validation.validate(film)) {
            map.addValue("id", id);
            map.addValue("name", film.getName());
            map.addValue("description", film.getDescription());
            map.addValue("release_date", film.getReleaseDate());
            map.addValue("duration", film.getDuration());
            map.addValue("mpa_id", film.getMpa().getId());
            map.addValue("likes", film.getLikes());
        }
        jdbcOperations.update(sqlQuery, map);

        genreStorage.deleteGenreOfFilm(film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {

            List<Integer> genreIdList = film.getGenres().stream()
                    .map(Genre::getId)
                    .distinct()
                    .collect(Collectors.toList());

            for (Integer genre : genreIdList) {
                genreStorage.addGenreForFilm(genre, film.getId());
            }

            film.setGenres(genreStorage.findGenreByFilmId(film.getId()));
        }
        film.setMpa(mpaStorage.findMpaNameById(film.getMpa().getId()));
        return film;
    }

    @Override
    public List<Film> findAll() throws NotFoundException {
        String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.LIKES," +
                "  M.MPA_ID, M.MPA_NAME from FILMS f INNER JOIN MPA M on M.MPA_ID = f.MPA_ID ORDER BY f.FILM_ID";
        return jdbcOperations.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film findFilmById(Integer id) throws NotFoundException {
        String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.LIKES," +
                "f.MPA_ID, M.MPA_NAME from FILMS f INNER JOIN MPA M on f.MPA_ID = M.MPA_ID where f.FILM_ID = :filmId";
        final List<Film> filmList = jdbcOperations.query(sqlQuery, Map.of("filmId", id), (rs, rowNum) -> makeFilm(rs));
        if (filmList.isEmpty()) {
            log.info("Фильм c идентификатором {} не найден в БД", id);
            throw new NotFoundException("Фильм с  таким ID не найден.");
        }
        return filmList.get(0);
    }

    @Override
    public Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("FILM_ID");
        Film film = new Film(id,
                rs.getString("FILM_NAME"),
                rs.getString("FILM_DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                rs.getInt("LIKES"),
                new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME"))
        );
        film.setGenres(genreStorage.findGenreByFilmId(id));
        return film;
    }
}
