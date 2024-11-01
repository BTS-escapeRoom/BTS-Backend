package com.bangtalboys.BTS_Backend.theme.dto;

import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ThemeResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String thumbnail;

    private String title;

    private String description;

    private int minimumPeople;

    private int maximumPeople;

    private int recommendPeople;

    private Long difficulty;

    private String genre;

    private int fearLevel;

    private int time;

    private int price;

    private String reservationUrl;

    private String notes;

    private LocalDateTime registrationDate;


    @Builder
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
    }
}
