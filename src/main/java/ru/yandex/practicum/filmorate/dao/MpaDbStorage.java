package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDbStorage {
    List<Mpa> getAll();

    Mpa getBYId(Integer id);
}
