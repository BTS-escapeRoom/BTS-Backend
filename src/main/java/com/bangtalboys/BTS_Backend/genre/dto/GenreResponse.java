package com.bangtalboys.BTS_Backend.genre.dto;

import com.bangtalboys.BTS_Backend.genre.domain.Genre;
import lombok.Data;

@Data
public class GenreResponse {
    private Long id;
    private String name;

    public GenreResponse(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
    }
}
