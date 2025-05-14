package com.bangtalboys.BTS_Backend.theme.dto;

import lombok.Data;

import java.util.List;

@Data
public class ThemeListPageResponse {
    private List<ThemeListResponse> themes;
    private long nextPage;
    private long totalPage;

    public ThemeListPageResponse(List<ThemeListResponse> themes, long nextPage, long totalPage) {
        this.themes = themes;
        this.nextPage = nextPage;
        this.totalPage = totalPage;
    }
}
