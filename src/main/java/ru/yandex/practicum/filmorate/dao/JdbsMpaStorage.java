package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j

@RequiredArgsConstructor
public class JdbsMpaStorage implements MpaDbStorage {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "select * from MPA";
        return jdbcOperations.query(sqlQuery, (rs, rowNum) -> makeMpa(rs));
    }

    public Mpa findMpaNameById(Integer id) {
        String sqlQuery = "select * from MPA where MPA_ID = :id";
        List<Mpa> mpaList = jdbcOperations.query(sqlQuery, Map.of("id", id), (rs, rowNum) -> makeMpa(rs));
        if (mpaList.isEmpty()) {
            log.info("Rating with id {} not found.", id);
            throw new NotFoundException("МРА с таким ID не найден.");
        }
        return mpaList.get(0);
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME")
        );
    }
}
