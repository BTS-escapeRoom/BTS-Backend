package com.bangtalboys.BTS_Backend.genre.service;

import com.bangtalboys.BTS_Backend.genre.domain.Genre;
import com.bangtalboys.BTS_Backend.genre.dto.GenreResponse;
import com.bangtalboys.BTS_Backend.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {

    private final GenreRepository genreRepository;

    public List<GenreResponse> getAllGenre() {
        List<Genre> genreList = genreRepository.findAll();
        return genreList.stream().map(GenreResponse::new).collect(Collectors.toList());
    }
}
