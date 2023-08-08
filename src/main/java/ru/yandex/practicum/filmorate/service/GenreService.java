package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDbStorage genreDbStorage;

    public List<Genre> findAllGenre() {
        return genreDbStorage.findAllGenre();
    }

    public Genre findGenreByIds(Integer id) {
        return genreDbStorage.findGenreByIds(id);
    }

    public List<Genre> findGenreByFilmId(Integer id) {
        return genreDbStorage.findGenreByFilmId(id);
    }


}
