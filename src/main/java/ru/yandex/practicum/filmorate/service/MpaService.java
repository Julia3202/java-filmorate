package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@AllArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAll();
    }

    public Mpa findMpaNameById(Integer id) {
        return mpaStorage.findMpaNameById(id);
    }
}
