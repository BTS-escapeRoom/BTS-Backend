package com.bangtalboys.BTS_Backend.theme.dto;

import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ThemeListResponse {
    private Long id;
    private String thumbnail;
    private String title;
    private Integer minimumPeople;
    private Integer maximumPeople;
    private Long difficulty;
    private String genre;
    private Integer time;
    private String genreType;

    @Builder
    public ThemeListResponse(Theme theme) {
        id = theme.getId();
        thumbnail = theme.getThumbnail();
        title = theme.getTitle();
        minimumPeople = theme.getMinimumPeople();
        maximumPeople = theme.getMaximumPeople();
        difficulty = theme.getDifficulty();
        genre = theme.getGenre();
        time = theme.getTime();
        genreType = theme.getGenreType().getName();
    }
}
