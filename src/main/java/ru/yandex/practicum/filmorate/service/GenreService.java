package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<Genre> findAllGenre() {
        return genreStorage.findAllGenre();
    }

    public Genre findGenreByIds(Integer id) {
        return genreStorage.findGenreByIds(id);
    }

    public List<Genre> findGenreByFilmId(Integer id) {
        return genreStorage.findGenreByFilmId(id);
    }


}
