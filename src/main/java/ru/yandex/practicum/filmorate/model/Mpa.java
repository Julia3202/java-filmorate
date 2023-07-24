package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mpa {

    private Integer id;
    private String name;

    public Mpa(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
