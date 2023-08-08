package ru.yandex.practicum.filmorate.databaseTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.JdbcFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFilmStorageTest {
    private final JdbcFilmStorage filmStorage;

    private Film film;
    private Mpa mpa;

    @BeforeEach
    public void beforeEach() {
        LocalDate date = LocalDate.of(2012, 5, 4);
        mpa = new Mpa(5, "R-13");
        film = new Film(1, "testFilm", "testDescription", date, 1000L, 5, mpa);
        film.setGenres(List.of(new Genre(1, null)));
    }

    @Test
    void createFilmTest() {
        Film newFilm = filmStorage.create(film);

        assertThat(newFilm)
                .hasFieldOrPropertyWithValue("id", 4)
                .hasFieldOrPropertyWithValue("name", "testFilm")
                .hasFieldOrPropertyWithValue("description", "testDescription")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2012, 5, 4))
                .hasFieldOrPropertyWithValue("duration", 1000L)
                .hasFieldOrPropertyWithValue("likes", 5);

        Integer genreId = newFilm.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList()).get(0);
        assertThat(genreId).isEqualTo(1);
        Integer mpaId = newFilm.getMpa().getId();
        assertThat(mpaId).isEqualTo(5);
    }

    @Test
    void update() {
        filmStorage.create(film);
        Film newFilm = new Film(1, "newFilm", "newDescription",
                LocalDate.of(2005, 12, 30), 1000L, 5, mpa);
        newFilm.setGenres(List.of(new Genre(1, null), new Genre(2, null)));

        Film testFilm = filmStorage.update(newFilm);

        assertThat(testFilm)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "newFilm")
                .hasFieldOrPropertyWithValue("description", "newDescription")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2005, 12, 30))
                .hasFieldOrPropertyWithValue("duration", 1000L)
                .hasFieldOrPropertyWithValue("likes", 5);
        Integer mpaId = newFilm.getMpa().getId();
        assertThat(mpaId).isEqualTo(5);
    }

    @Test
    void findAll() {
        filmStorage.create(film);
        List<Film> filmList = filmStorage.findAll();
        assertThat(filmList.size()).isEqualTo(1);
    }

    @Test
    void findFilmById() {
        filmStorage.create(film);
        Film filmStorageFilmById = filmStorage.findFilmById(1);

        assertThat(filmStorageFilmById)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "newFilm")
                .hasFieldOrPropertyWithValue("description", "newDescription")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2005, 12, 30))
                .hasFieldOrPropertyWithValue("duration", 1000L)
                .hasFieldOrPropertyWithValue("likes", 5);
    }
}