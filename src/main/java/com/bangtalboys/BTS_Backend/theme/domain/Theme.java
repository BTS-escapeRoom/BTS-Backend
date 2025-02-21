package com.bangtalboys.BTS_Backend.theme.domain;

import com.bangtalboys.BTS_Backend.genre.domain.Genre;
import com.bangtalboys.BTS_Backend.store.domain.Store;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name = "theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private LocalDateTime registrationDate;

    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genreType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "theme", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<ThemeTime> themeTimeList = new ArrayList<>();
}
