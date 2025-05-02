package com.bangtalboys.BTS_Backend.theme.repository;

import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeListRequest;

import java.util.List;

public interface ThemeCustomRepository {
    List<Theme> findThemes(ThemeListRequest themeListRequest);
}