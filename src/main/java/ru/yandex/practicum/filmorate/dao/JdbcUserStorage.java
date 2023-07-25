package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class JdbcUserStorage implements UserDbStorage {
    private final UserValidation validation = new UserValidation();
    private final NamedParameterJdbcOperations jdbcOperations;
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserStorage(NamedParameterJdbcOperations jdbcOperations, JdbcTemplate jdbcTemplate) {
        this.jdbcOperations = jdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) throws ValidationException {
        String sqlQuery = "insert into USERS (USER_EMAIL, USER_NAME, USER_LOGIN, USER_BIRTHDAY)" +
                " values(:email, :name, :login, :birthday)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        if (validation.validate(user)) {
            map.addValue("email", user.getEmail());
            map.addValue("name", user.getName());
            map.addValue("login", user.getLogin());
            map.addValue("birthday", user.getBirthday());
            jdbcOperations.update(sqlQuery, map, keyHolder);
            user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        }
        return user;
    }

    @Override
    public User update(User user) {
        if (findUserById(user.getId()) == null) {
            throw new NotFoundException("Данный пользователь не был найден.");
        }
        String sqlQuery = "update USERS SET USER_EMAIL= :email , USER_NAME= :name, USER_LOGIN= :login," +
                " USER_BIRTHDAY = :birthday where USER_ID = :id";
        MapSqlParameterSource map = new MapSqlParameterSource();
        if (validation.validate(user)) {
            map.addValue("id", user.getId());
            map.addValue("email", user.getEmail());
            map.addValue("name", user.getName());
            map.addValue("login", user.getLogin());
            map.addValue("birthday", user.getBirthday());
            jdbcOperations.update(sqlQuery, map);
        }
        return user;
    }

    @Override
    public List<User> findAll() throws NotFoundException {
        String sqlQuery = "select USER_ID, USER_EMAIL, USER_NAME, USER_LOGIN, USER_BIRTHDAY FROM USERS";
        List<User> userList = jdbcTemplate.query(sqlQuery, this::mapRow);
        return userList;
    }

    @Override
    public User findUserById(Integer id) throws NotFoundException, ValidationException {
        String sqlQuery = "select USER_ID, USER_EMAIL, USER_NAME, USER_LOGIN, USER_BIRTHDAY " +
                "from USERS where USER_ID= :userId";
        final List<User> userList = jdbcOperations.query(sqlQuery, Map.of("userId", id), new UserRowMapper());
        if (userList.size() != 1) {
            throw new NotFoundException("Пользователь с таким ID не найден.");
        }
        return userList.get(0);
    }

    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("USER_EMAIL"),
                rs.getString("USER_LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("USER_BIRTHDAY").toLocalDate()
        );
    }
}