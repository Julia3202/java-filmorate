package ru.yandex.practicum.filmorate.databaseTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserStorage userStorage;

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
        User users = userStorage.create(user);
        assertThat(users)
                .hasFieldOrPropertyWithValue("email", "userMail@mail.ru")
                .hasFieldOrPropertyWithValue("login", "userLogin")
                .hasFieldOrPropertyWithValue("name", "userName")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 15));
    }

    @Test
    void update() {
        LocalDate date = LocalDate.of(2000, 1, 15);
        userStorage.create(user);
        User newUser = userStorage.update(user);
        assertThat(newUser)
                .hasFieldOrPropertyWithValue("email", "userMail@mail.ru")
                .hasFieldOrPropertyWithValue("login", "userLogin")
                .hasFieldOrPropertyWithValue("name", "userName")
                .hasFieldOrPropertyWithValue("birthday", date);
    }

    @Test
    void findAll() {
        userStorage.create(user);
        List<User> userList = userStorage.findAll();
        assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    void findUserById() {
        userStorage.create(user);
        Optional<User> users = userStorage.findUserById(1);
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
        userStorage.create(user);
        userStorage.create(friend);
        userId = user.getId();
        friendId = friend.getId();
        userStorage.addFriends(userId, friendId);
        List<User> friendList = userStorage.getAllFriends(userId);
        assertThat(friendList.size()).isEqualTo(1);
    }

    @Test
    void removeFriends() {
        addFriends();
        userStorage.removeFriends(userId, friendId);
        List<User> friendList = userStorage.getAllFriends(userId);
        assertThat(friendList.size()).isEqualTo(0);
    }

    @Test
    void getMutualFriend() {
        userStorage.create(user);
        userStorage.create(friend);
        userId = user.getId();
        friendId = friend.getId();
        userStorage.addFriends(userId, friendId);
        User userTest = new User("test@mail.ru", "testLogin",
                "testName", LocalDate.of(2001, 2, 12));
        userStorage.create(userTest);
        userStorage.addFriends(userTest.getId(), friendId);
        List<User> mutualFriend = userStorage.getMutualFriend(userTest.getId(), userId);
        assertThat(mutualFriend.size()).isEqualTo(1);
        assertThat(mutualFriend.get(0).getId()).isEqualTo(10);
    }
}