package com.bangtalboys.BTS_Backend.theme.dto;

import com.bangtalboys.BTS_Backend.store.dto.StoreListResponse;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.domain.ThemeTime;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ThemeResponse {

    private Long id;
    private String thumbnail;
    private String title;
    private String description;
    private Integer minimumPeople;
    private Integer maximumPeople;
    private Integer recommendPeople;
    private Long difficulty;
    private String genre;
    private Integer fearLevel;
    private Integer time;
    private Integer price;
    private String reservationUrl;
    private String notes;
    private LocalDateTime registrationDate;
    private String genreType;
    private StoreListResponse store;
    private List<ThemeTimeResponse> weekdaysTimeList;
    private List<ThemeTimeResponse> weekendTimeList;


    public ThemeResponse(Theme theme) {
        id = theme.getId();
        thumbnail = theme.getThumbnail();
        title = theme.getTitle();
        description = theme.getDescription();
        minimumPeople = theme.getMinimumPeople();
        maximumPeople = theme.getMaximumPeople();
        difficulty = theme.getDifficulty();
        genre = theme.getGenre();
        fearLevel = theme.getFearLevel();
        time = theme.getTime();
        price = theme.getPrice();
        reservationUrl = theme.getReservationUrl();
        notes = theme.getNotes();
        registrationDate = theme.getRegistrationDate();
        genreType = theme.getGenreType().getName();
        store = new StoreListResponse(theme.getStore());
        weekdaysTimeList = theme.getThemeTimeList().stream()
                .filter(themeTime -> themeTime.getTimeType().equals("weekdays"))
                .sorted(Comparator.comparing(ThemeTime::getTime))
                .map(ThemeTimeResponse::new).collect(Collectors.toList());
        weekendTimeList = theme.getThemeTimeList().stream()
                .filter(themeTime -> themeTime.getTimeType().equals("weekend"))
                .sorted(Comparator.comparing(ThemeTime::getTime))
                .map(ThemeTimeResponse::new).collect(Collectors.toList());

    }
}
