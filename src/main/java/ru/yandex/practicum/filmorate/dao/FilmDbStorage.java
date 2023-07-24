package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmDbStorage {

    Film create(Film film) throws ValidationException;

    Film update(Film film) throws ValidationException;

    List<Film> findAll() throws NotFoundException;

    Film findFilmById(Integer id) throws NotFoundException;
}