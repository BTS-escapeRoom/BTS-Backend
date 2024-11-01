package com.bangtalboys.BTS_Backend.theme.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchThemeRequest {
    private String title;
    private int peoples;
    private String genre;
    private int difficulty;
    private Long district_id;
    private Long city_id;

    //TODO: 정렬기능 추가 필요
}
