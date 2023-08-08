package ru.yandex.practicum.filmorate.databaseTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcUserStorageTest {

    private final UserDbStorage userDbStorage;

    private User user;
    private User friend;

    private Integer userId;
    private Integer friendId;

    @BeforeEach
    public void beforeEach() {
        LocalDate date = LocalDate.of(2000, 1, 15);
        LocalDate friendBirthday = LocalDate.of(2000, 1, 15);
        user = new User("userMail@mail.ru", "userLogin", "userName", date);
        friend = new User("friendMail@mail.ru", "friendLogin", "friendName", friendBirthday);
    }

    @Test
    void create() {
        User users = userDbStorage.create(user);
        assertThat(users)
                .hasFieldOrPropertyWithValue("email", "userMail@mail.ru")
                .hasFieldOrPropertyWithValue("login", "userLogin")
                .hasFieldOrPropertyWithValue("name", "userName")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 15));
    }

    @Test
    void update() {
        LocalDate date = LocalDate.of(2000, 1, 15);
        userDbStorage.create(user);
        User newUser = userDbStorage.update(user);
        assertThat(newUser)
                .hasFieldOrPropertyWithValue("email", "userMail@mail.ru")
                .hasFieldOrPropertyWithValue("login", "userLogin")
                .hasFieldOrPropertyWithValue("name", "userName")
                .hasFieldOrPropertyWithValue("birthday", date);
    }

    @Test
    void findAll() {
        userDbStorage.create(user);
        List<User> userList = userDbStorage.findAll();
        assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    void findUserById() {
        userDbStorage.create(user);
        Optional<User> users = userDbStorage.findUserById(1);
        assertThat(users)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "userMail@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "userLogin")
                                .hasFieldOrPropertyWithValue("name", "userName")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 15))
                );
    }

    @Test
    void addFriends() {
        userDbStorage.create(user);
        userDbStorage.create(friend);
        userId = user.getId();
        friendId = friend.getId();
        userDbStorage.addFriends(userId, friendId);
        List<User> friendList = userDbStorage.getAllFriends(userId);
        assertThat(friendList.size()).isEqualTo(1);
    }

    @Test
    void removeFriends() {
        addFriends();
        userDbStorage.removeFriends(userId, friendId);
        List<User> friendList = userDbStorage.getAllFriends(userId);
        assertThat(friendList.size()).isEqualTo(0);
    }

    @Test
    void getMutualFriend() {
        userDbStorage.create(user);
        userDbStorage.create(friend);
        userId = user.getId();
        friendId = friend.getId();
        userDbStorage.addFriends(userId, friendId);
        User userTest = new User("test@mail.ru", "testLogin",
                "testName", LocalDate.of(2001, 2, 12));
        userDbStorage.create(userTest);
        userDbStorage.addFriends(userTest.getId(), friendId);
        List<User> mutualFriend = userDbStorage.getMutualFriend(userTest.getId(), userId);
        assertThat(mutualFriend.size()).isEqualTo(1);
        assertThat(mutualFriend.get(0).getId()).isEqualTo(10);
    }
}