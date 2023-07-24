package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Map;

@Component
public class JdbsMpaStorage implements MpaDbStorage {

    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbsMpaStorage(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcOperations.query("select MPA_ID, MPA_NAME from MPA", new MpaRowMapper());
    }

    @Override
    public Mpa getBYId(Integer id) {
        String sqlQuery = "select MPA_ID, MPA_NAME from MPA where MPA_ID = :id";
        final List<Mpa> mpaList = jdbcOperations.query(sqlQuery, Map.of("id", id), new MpaRowMapper());
        if (mpaList.size() != 1) {
            throw new NotFoundException("Rating with id {} " + id + " not found.");
        }
        return mpaList.get(0);
    }
}
