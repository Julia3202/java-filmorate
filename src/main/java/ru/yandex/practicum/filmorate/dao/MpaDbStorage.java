package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDbStorage {
    List<Mpa> getAll();

    Mpa findMpaNameById(Integer id);
}
