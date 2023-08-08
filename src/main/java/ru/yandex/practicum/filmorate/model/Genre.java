package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public class Genre {

    private final Integer id;
    private final String name;

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
