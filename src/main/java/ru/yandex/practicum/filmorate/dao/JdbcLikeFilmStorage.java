package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JdbcLikeFilmStorage implements LikeFilmStorage {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userStorage;

    @Override
    public List<Film> findPopularFilm(Integer count) {
        String sqlQuery = ("select f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.LIKES," +
                "f.MPA_ID, M.MPA_NAME, count(l.USER_ID) AS rate from FILMS f " +
                "INNER JOIN MPA M on f.MPA_ID = M.MPA_ID " +
                "left join FILM_LIKE l on f.FILM_ID = l.FILM_ID " +
                "group by f.FILM_ID " +
                "order by rate desc limit :count");
        return jdbcOperations.query(sqlQuery, Map.of("count", count)
                , (rs, rowNum) -> filmDbStorage.makeFilm(rs));
    }

    @Override
    public void likeFilm(Integer id, Integer userId) throws OtherException {
        if (!findUserLike(id, userId).isEmpty()) {
            throw new OtherException("Нельзя оценивать фильм больше 1 раза.");
        }
        String query = "insert into FILM_LIKE(FILM_ID, USER_ID) values(:filmId, :userId)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("filmId", id);
        map.addValue("userId", userId);
        jdbcOperations.update(query, map);
    }

    @Override
    public void removeLikeFilm(Integer id, Integer userId) throws OtherException {
        if (findUserLike(id, userId).isEmpty()) {
            throw new OtherException("Нельзя удалить оценку, если она не была поставлена.");
        }
        String query = "delete from FILM_LIKE where FILM_ID = :filmId and USER_ID = :userId";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("filmId", id);
        map.addValue("userId", userId);
        jdbcOperations.update(query, map);
    }

    @Override
    public List<Integer> findUserLike(Integer id, Integer userId) {
        if (userStorage.findUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (filmDbStorage.findFilmById(id) == null) {
            throw new NotFoundException("Фильм не найден.");
        }
        String sqlQuery = "select user_id from FILM_LIKE where FILM_ID = :filmId and USER_ID = :userId";
        return jdbcOperations.query(sqlQuery, Map.of("filmId", id, "userId", userId),
                (rs, rowNum) -> rs.getInt("USER_ID"));
    }
}
