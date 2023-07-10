package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum FilmCategory {
    COMEDY("Комедия"),
    DRAMA("Драма"),
    CARTOON("Мультфильм"),
    THRILLER("Триллер"),
    DOCUMENTARY("Документальный"),
    FIGHTER("Боевик");

    private final String nameCategory;

    FilmCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }
}
