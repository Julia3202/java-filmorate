package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final UserValidation validation = new UserValidation();
    private final NamedParameterJdbcOperations jdbcOperations;
    private final JdbcTemplate jdbcTemplate;

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
        if (findUserById(user.getId()).isEmpty()) {
            log.info("Пользователь с ID = {} не найден.", user.getId());
            throw new NotFoundException("Пользователь с ID = " + user.getId() + " не найден.");
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
        String sqlQuery = "select * FROM USERS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public Optional<User> findUserById(Integer id) throws NotFoundException, ValidationException {
        String sqlQuery = "select * from USERS where USER_ID= :userId";
        List<User> userList = jdbcOperations.query(sqlQuery, Map.of("userId", id), (rs, rowNum) -> makeUser(rs));
        if (!userList.isEmpty()) {
            return Optional.of(userList.get(0));
        }
        log.info("Пользователь с ID = {} не найден.", id);
        return Optional.empty();
    }

    @Override
    public void addFriends(Integer userId, Integer id) {
        if (findUserById(userId).isEmpty()) {
            log.info("Пользователь с ID = {} не найден.", userId);
            throw new NotFoundException("Пользователь с ID = " + userId + " не найден.");
        }
        if (findUserById(id).isEmpty()) {
            log.info("Пользователь с ID = {} не найден.", id);
            throw new NotFoundException("Пользователь с ID = " + id + " не найден.");
        }
        String sqlQuery = "insert into USER_FRIEND (USER_ID, FRIEND_ID) values (:userId, :friendId)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("friendId", id);
        jdbcOperations.update(sqlQuery, map);
    }

    @Override
    public void removeFriends(Integer id, Integer friendId) {
        if (findUserById(friendId).isEmpty()) {
            log.info("Пользователь с ID = {} не найден.", friendId);
            throw new NotFoundException("Пользователь с ID = " + friendId + " не найден.");
        }
        if (findUserById(id).isEmpty()) {
            log.info("Пользователь с ID = {} не найден.", id);
            throw new NotFoundException("Пользователь с ID = " + id + " не найден.");
        }
        String sqlQuery = "delete from USER_FRIEND where USER_ID = :userId and FRIEND_ID = :friendId";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", id);
        map.addValue("friendId", friendId);
        jdbcOperations.update(sqlQuery, map);
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        if (findUserById(id).isEmpty()) {
            log.info("Пользователь с ID = {} не найден.", id);
        }
        String sqlQuery = "select u.* from USER_FRIEND uf join USERS u on uf.FRIEND_ID = u.USER_ID where uf.USER_ID = :id";

        return jdbcOperations.query(sqlQuery, Map.of("id", id), (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public List<User> getMutualFriend(Integer id, Integer otherId) throws NotFoundException {
        if (findUserById(otherId).isEmpty()) {
            log.info("Пользователь с ID = {} не найден.", otherId);
            throw new NotFoundException("Пользователь с ID = " + otherId + " не найден.");
        }
        if (findUserById(id).isEmpty()) {
            log.info("Пользователь с ID = {} не найден.", id);
            throw new NotFoundException("Пользователь с ID = " + id + " не найден.");
        }
        if (getAllFriends(id).isEmpty()) {
            log.info("У пользователя с ID = {} нет друзей в списке.", id);
            throw new NotFoundException("У пользователя с ID = " + id + " нет друзей в списке.");
        }
        if (getAllFriends(otherId).isEmpty()) {
            log.info("У пользователя с ID = {} нет друзей в списке.", otherId);
            throw new NotFoundException("У пользователя с ID = " + otherId + " нет друзей в списке.");
        }
        String query = "SELECT * from USERS where USER_ID in" +
                " (select FRIEND_ID from USER_FRIEND where USER_ID = :userId " +
                "intersect  select FRIEND_ID from USER_FRIEND where USER_ID = :otherId)";
        return jdbcOperations.query(query, Map.of("userId", id, "otherId", otherId),
                (rs, rowNum) -> makeUser(rs));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("USER_EMAIL"),
                rs.getString("USER_LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("USER_BIRTHDAY").toLocalDate()
        );
    }
}
