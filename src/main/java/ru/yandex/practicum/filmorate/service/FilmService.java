package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findFilmById(Integer id) {
        return filmStorage.findFilmById(id);
    }

    public Map<Integer, Film> getFilms() throws NotFoundException {
        if (filmStorage.findAll() == null) {
            throw new NotFoundException("Список фильмов пуст.");
        }
        return filmStorage.getFilms();
    }

    public Map<Integer, User> getUsers() throws NotFoundException {
        if (userStorage.findAll() == null) {
            throw new NotFoundException("Список пользователей пуст.");
        } else {
            return userStorage.getUsers();
        }
    }

    public List<Integer> likeFilm(Integer id, Integer userId) throws OtherException {
        if (!getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (!getFilms().containsKey(id)) {
            throw new NotFoundException("Фильм не найден.");
        }
        Film film = getFilms().get(id);
        List<Integer> filmLike = film.getUserLike();
        if (!(filmLike.isEmpty()) && (filmLike.contains(userId))) {
            throw new OtherException("Нельзя оценивать фильм больше 1 раза.");
        }
        filmLike.add(userId);
        int like = film.getLike() + 1;
        film.setLike(like);
        return filmLike;
    }

    public void removeLikeFilm(Integer id, Integer userId) throws OtherException {
        if (!getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (!getFilms().containsKey(id)) {
            throw new NotFoundException("Фильм не найден.");
        }
        Film film = getFilms().get(id);
        if (!film.getUserLike().contains((userId))) {
            throw new OtherException("Нельзя удалить оценку, если она не была поставлена.");
        }
        film.getUserLike().remove(userId);
        int like = film.getLike() - 1;
        film.setLike(like);
    }


    public List<Film> findPopularFilm(Integer count) {
        int finalCount = count;
        List<Film> filmList = new ArrayList<>(getFilms().values());
        Collections.reverse(filmList);
        List<Film> popularFilms = new ArrayList<>();
        if (finalCount == 1) {
            popularFilms.add(filmList.get(0));
        } else if (finalCount < filmList.size()) {
            for (int i = 0; i < finalCount; i++) {
                popularFilms.add(filmList.get(i));
            }
        } else {
            popularFilms.addAll(filmList);
        }
        return popularFilms;
    }
}
