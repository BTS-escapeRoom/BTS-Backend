package com.bangtalboys.BTS_Backend.theme.domain;

import com.bangtalboys.BTS_Backend.store.domain.Store;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

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

    private int minimumPeople;

    private int maximumPeople;

    private int recommendPeople;

    private Long difficulty;

    private String genre;

    private int fearLevel;

    private int time;

    private int price;

    private String reservationUrl;

    private LocalDateTime registrationDate;

    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
