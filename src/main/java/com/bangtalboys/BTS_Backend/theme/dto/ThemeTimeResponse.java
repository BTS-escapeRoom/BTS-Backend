package com.bangtalboys.BTS_Backend.theme.dto;

import com.bangtalboys.BTS_Backend.theme.domain.ThemeTime;
import lombok.Data;

@Data
public class ThemeTimeResponse {
    private Long id;
    private String time;

    public ThemeTimeResponse(ThemeTime themeTime) {
        id = themeTime.getId();
        time = themeTime.getTime();
    }
}

