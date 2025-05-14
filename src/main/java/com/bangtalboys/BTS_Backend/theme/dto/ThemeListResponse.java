package com.bangtalboys.BTS_Backend.theme.dto;

import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import lombok.Data;

@Data
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
    private String status;
    private String store;
    private String city;
    private String district;


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
        store = theme.getStore().getName();
        city = theme.getStore().getDistrict().getCity().getName();
        district = theme.getStore().getDistrict().getName();
    }
}