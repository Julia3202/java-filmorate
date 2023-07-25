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
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {
    private final UserDbStorage userStorage;

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations jdbcOperations;

    public UserService(JdbcTemplate jdbcTemplate, UserDbStorage userStorage, NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
        this.jdbcOperations = jdbcOperations;
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
                "where FRIEND_ID = ? and USER_ID =?", friendId, id);
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
            String sqlQuerys = "delete from USER_FRIEND where FRIEND_ID=? ";
            jdbcTemplate.update(sqlQuerys, friendId);
        } else {
            log.info("Пользователь {} не найден у вас в друзьях.", friendId);
        }
        return findUserById(id);
    }

    public List<User> getAllFriends(Integer id) {
        if (findUserById(id) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }
        String sqlQuery = "select u.USER_ID, u.USER_BIRTHDAY, u.USER_EMAIL, u.USER_LOGIN, u.USER_NAME " +
                "from USER_FRIEND uf join USERS u on uf.FRIEND_ID = u.USER_ID where u.USER_ID =:id";
        /*if (jdbcTemplate.query(sqlQuery, new UserRowMapper(), id) == null) {
            log.info("У Вас нет друзей в списке.");
            return new ArrayList<User>();
        }*/
        List<User> users = jdbcOperations.query(sqlQuery, Map.of("id", id) , (rs, mapRow) -> makeUser(rs));

        return users;
    }

    private User makeUser(ResultSet rs) throws SQLException{
        return new User(
                rs.getInt("USER_ID"),
                rs.getString("USER_EMAIL"),
                rs.getString("USER_LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
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
