package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.OtherException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        if (getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (getFilms().containsKey(id)) {
            throw new NotFoundException("Фильм не найден.");
        }
        Film film = getFilms().get(id);
        if (film.getUserLike().get(userId) != null) {
            throw new OtherException("Нельзя оценивать фильм больше 1 раза.");
        }
        film.getUserLike().add(userId);
        int like = film.getLike() + 1;
        film.setLike(like);
        return film.getUserLike();
    }

    public void removeFilm(Integer id, Integer userId) throws OtherException {
       /*if (getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }*/
        if (getFilms().containsKey(id)) {
            throw new NotFoundException("Фильм не найден.");
        }
        Film film = getFilms().get(id);
        if (film.getUserLike().get(userId) == null) {
            throw new OtherException("Нельзя удалить оценку, если она не была поставлена.");
        }
        film.getUserLike().remove(userId);
        int like = film.getLike() - 1;
        film.setLike(like);
    }


    public List<Film> findListFirstFilm(Integer count) {
        if (count == null) {
            count = 10;
        }
        int finalCount = count;
        List<Film> filmList = new ArrayList<>();
        Set<Film> films = new TreeSet<>(Comparator.comparing(Film::getLike));
        for (int i = 1; i == finalCount; i++) {
            for (Film film : films) {
                filmList.add(film);
            }
        }
        return filmList;
    }
}