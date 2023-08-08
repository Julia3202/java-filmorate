package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JdbsGenreStorage implements GenreDbStorage {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Genre findGenreByIds(Integer id) {
        String sqlQuery = "select* from GENRE where GENRE_ID = :id";
        List<Genre> genres = jdbcOperations.query(sqlQuery, Map.of("id", id), (rs, rowNum) -> makeGenre(rs));
        if (genres.isEmpty()) {
            log.info("Genre with id {} not found.", id);
            throw new NotFoundException("Жанр не найден.");
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> findAllGenre() {
        String sqlQuery = "select * from GENRE";
        return jdbcOperations.query(sqlQuery, ((rs, rowNum) -> makeGenre(rs)));
    }

    @Override
    public List<Genre> findGenreByFilmId(Integer id) {
        String sqlQuery = "select fg.GENRE_ID, g.GENRE_NAME from FILM_GENRE fg " +
                "join GENRE g on fg.GENRE_ID = g.GENRE_ID " +
                "where fg.FILM_ID = :id order by g.GENRE_ID";
        List<Genre> genreList = jdbcOperations.query(sqlQuery, Map.of("id", id), (rs, rowNum) -> makeGenre(rs));
        if (genreList.isEmpty()) {
            log.info("Genre with id {} not found.", id);
            return new ArrayList<>();
        }
        return genreList;
    }

    @Override
    public void deleteGenreOfFilm(Integer id) {
        String sqlQuery = "delete from FILM_GENRE where FILM_ID = :id";
        jdbcOperations.update(sqlQuery, Map.of("id", id));
    }

    @Override
    public void addGenreForFilm(Integer idGenre, Integer idFilm) {
        if (findGenreByIds(idGenre) == null) {
            throw new NotFoundException("Жанр не найден.");
        }
        String sqlQuery = "insert into FILM_GENRE(FILM_ID, GENRE_ID) values (:filmId, :genreId)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("filmId", idFilm);
        map.addValue("genreId", idGenre);
        jdbcOperations.update(sqlQuery, map);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }
}