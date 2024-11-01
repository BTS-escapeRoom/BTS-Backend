package com.bangtalboys.BTS_Backend.theme.service;

import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeResponse;
import com.bangtalboys.BTS_Backend.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;

    public List<ThemeResponse> getThemes(String title, int peoples, String genre, int difficulty, Long districtId, Long cityId) {
        List<Theme> themes = themeRepository.findByTitleAndPeoplesAndGenreAndDifficultyAndDistrictOrCity(title, peoples, genre, difficulty, districtId, cityId);
        return themes.stream().map(ThemeResponse::new).collect(Collectors.toList());
    }

};

