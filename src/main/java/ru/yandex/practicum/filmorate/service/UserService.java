package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.dao.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserDbStorage userStorage;

    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate, UserDbStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findUserById(Integer id) {
        return userStorage.findUserById(id);
    }


    public User addFriends(Integer friendId, Integer id) {
        if ((findUserById(friendId) == null) || (findUserById(id) == null)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        SqlRowSet sqlQuery = jdbcTemplate.queryForRowSet("select USER_ID, FRIEND_ID " +
                "from USER_FRIEND " +
                "where FRIEND_ID = ?", friendId);
        if (sqlQuery.next()) {
            log.info("Пользователь {} уже есть у вас в друзьях.", friendId);
        } else {
            String sqlQuerys = "insert into USER_FRIEND  (FRIEND_ID, USER_ID ) values ( ?, ? )";
            jdbcTemplate.update(sqlQuerys, friendId, id);
        }
        return findUserById(id);


    }

    public User removeFriends(Integer friendId, Integer id) {
        if ((findUserById(friendId) == null) || (findUserById(id) == null)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        SqlRowSet sqlQuery = jdbcTemplate.queryForRowSet("select USER_ID, FRIEND_ID " +
                "from USER_FRIEND " +
                "where FRIEND_ID = ?", friendId);
        if (sqlQuery.next()) {
            String sqlQuerys = "delete from USER_FRIEND where FRIEND_ID=? AND USER_ID=? ";
            jdbcTemplate.update(sqlQuerys, friendId, id);
        } else {
            log.info("Пользователь {} уже есть у вас в друзьях.", friendId);
        }
        return findUserById(id);
    }

    public List<User> getAllFriends(Integer id) {
        if (findUserById(id) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
        String sqlQuery = "SELECT USERS.USER_ID, USERS.USER_NAME, USERS.USER_LOGIN, USERS.USER_BIRTHDAY, USERS.USER_EMAIL " +
                "from USERS " +
                "RIGHT JOIN USER_FRIEND UF on USERS.USER_ID = UF.FRIEND_ID where USERS.USER_ID =?";
       if(jdbcTemplate.query(sqlQuery, new UserRowMapper(), id) == null){
           log.info("У Вас нет друзей в списке.");
           return new ArrayList<>();
       }
       return jdbcTemplate.query(sqlQuery, new UserRowMapper(), id);
    }

    public List<User> getMutualFriend(Integer id, Integer otherId) throws NotFoundException {
        if ((findUserById(otherId) == null) || (findUserById(id) == null)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if ((getAllFriends(id).isEmpty()) || (getAllFriends(otherId).isEmpty())) {
            log.info("У Вас нет друзей в списке.");
        }
        String query = "SELECT USER_ID, USER_NAME, USER_LOGIN, USER_BIRTHDAY, USER_EMAIL from USERS where USER_ID in" +
                " (select f.FRIEND_ID from USER_FRIEND f  " +
                "inner join USER_FRIEND u ON f.FRIEND_ID = u.FRIEND_ID where f.USER_ID=? AND u.USER_ID=?)";
        return jdbcTemplate.query(query, new UserRowMapper(), id, otherId);
    }
}