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
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage){
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilms() throws NotFoundException {
        if(filmStorage.findAll() == null){
            throw new NotFoundException("Список фильмов пуст.");
        }
        return filmStorage.findAll();
    }

    public List<User> getUsers() throws NotFoundException {
        if (userStorage.findAll() == null) {
            throw new NotFoundException("Список пользователей пуст.");
        } else {
            return userStorage.findAll();
        }
    }

    public Integer likeFilm(Integer id, Integer userId) throws OtherException {
        if (!getUsers().contains(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if(!getFilms().contains(id)){
            throw new NotFoundException("Фильм не найден.");
        }
        Film film = getFilms().get(id);
        if(film.getUserLike().contains(userId)){
            throw new OtherException("Нельзя оценивать фильм больше 1 раза.");
        }
        film.getUserLike().add(userId);
        return film.getUserLike().size();
    }

    public Integer removeFilm(Integer id, Integer userId) throws OtherException {
        if (!getUsers().contains(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if(!getFilms().contains(id)){
            throw new NotFoundException("Фильм не найден.");
        }
        Film film = getFilms().get(id);
        if(film.getUserLike().contains(userId)){
            throw new OtherException("Нельзя удалить оценку, если она не была поставлена.");
        }
        film.getUserLike().remove(userId);
        return film.getUserLike().size();
    }


    public List<Film> findListFirstFilm(Integer count) {
        if(count == null){
            count = 10;
        }
        Integer finalCount = count;
        List<Film> films = getFilms();
        return films.stream().sorted((f1, f2) -> {
            int comp = f1.getLike().compareTo(f2.getLike());
            List<Film> filmSort = new ArrayList<>();
            for(int i = 0; i == finalCount-1; i++){
                filmSort.add(films.get(i));
            }
            return comp;
        }).limit(count).collect(Collectors.toList());
       /* Collections.sort(films, new Comparator<Film>(){
            public int compare(Film f1, Film f2){
                return f1.getLike().compareTo(f2.getLike());
            }
        });*/
    }
}