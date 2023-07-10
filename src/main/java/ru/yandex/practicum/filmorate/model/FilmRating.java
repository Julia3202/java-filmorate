package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum FilmRating {
    G("G"),
    PG("PG"),
    PG13("PG-13"),
    R("R"),
    NC17("NC-17");

    private final String nameRating;

    FilmRating(String nameRating) {
        this.nameRating = nameRating;
    }
}
