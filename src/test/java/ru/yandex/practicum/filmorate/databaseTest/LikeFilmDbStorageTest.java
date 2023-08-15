package ru.yandex.practicum.filmorate.databaseTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.LikeFilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikeFilmDbStorageTest {

    private final FilmStorage filmStorage;
    private final LikeFilmStorage likeFilmStorage;
    private final UserStorage userStorage;

    @BeforeEach
    void beforeEach() {
        LocalDate date = LocalDate.of(2012, 5, 4);
        Mpa mpa = new Mpa(5, "R-13");
        Film film = new Film(1, "testFilm", "testDescription", date, 1000, 5, mpa);
        film.setGenres(List.of(new Genre(1, "Комедия")));
        filmStorage.create(film);

        User user = new User("userMail@mail.ru", "userLogin", "userName",
                LocalDate.of(2000, 1, 15));
        userStorage.create(user);
    }

    @Test
    void findPopularFilm() {
        likeFilmStorage.likeFilm(1, 1);
        List<Film> filmList = likeFilmStorage.findPopularFilm(5);
        assertThat(filmList.size()).isEqualTo(1);
    }

    @Test
    void likeFilm() {
        likeFilmStorage.likeFilm(1, 1);
        List<Integer> likeList = likeFilmStorage.findUserLike(1, 1);
        assertThat(likeList.size()).isEqualTo(1);
    }

    @Test
    void removeLikeFilm() {
        likeFilmStorage.likeFilm(1, 1);
        likeFilmStorage.removeLikeFilm(1, 1);

        List<Integer> likeList = likeFilmStorage.findUserLike(1, 1);
        assertThat(likeList.size()).isEqualTo(0);
    }
}