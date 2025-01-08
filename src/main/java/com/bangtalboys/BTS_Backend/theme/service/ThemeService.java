package com.bangtalboys.BTS_Backend.theme.service;

import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeListResponse;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeResponse;
import com.bangtalboys.BTS_Backend.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;

    public List<ThemeListResponse> getAllTheme(String title, Integer peoples, Integer difficulty, Long genreId, Long districtId, Long cityId) {
        List<Theme> themes = themeRepository.findByTitleAndPeoplesAndGenreAndDifficultyAndDistrictOrCity(title, peoples, difficulty, genreId, districtId, cityId);
        return themes.stream().map(ThemeListResponse::new).collect(Collectors.toList());
    }

    public ThemeResponse getOneTheme(Long id) {
        Optional<Theme> theme = themeRepository.findById(id);
        return theme.map(ThemeResponse::new).orElseThrow(NotFoundException::new);
    }

//    public ThemeResponse createThemeLike(Long memberId, Long ThemeId) {
//        ThemeLike themeLike = ThemeLike.builder()
//                .memberId(memberId)
//                .themeId(boardRequest.getThemeId())
//                .type(boardRequest.getType())
//                .title(boardRequest.getTitle())
//                .description(boardRequest.getDescription())
//                .hit(0L)
//                .build();
//        Optional<Theme> theme = themeRepository.save(id);
//        return theme.map(ThemeResponse::new).orElseThrow(NotFoundException::new);
//    }

    public List<ThemeListResponse> getLikeTheme(Long memberId) {
        List<Theme> themes = themeRepository.findLikeThemeByMemberId(memberId);
        return themes.stream().map(ThemeListResponse::new).collect(Collectors.toList());
    }


    public List<ThemeListResponse> getRandomThemes() {
        List<Theme> themes = new ArrayList<>();
        for (int i = 0; i <5; i++) {
            long randomWithMathRandom = (long) ((Math.random() * (423 - 1)) + 1);
            Theme defaultTheme = new Theme();
            Theme theme = themeRepository.findById(randomWithMathRandom).orElse(defaultTheme);
            themes.add(theme);
        }
        return themes.stream().map(ThemeListResponse::new).collect(Collectors.toList());
    }

    public List<ThemeListResponse> getPopularThemes() {
        Long[] ids = new Long[]{396L, 397L, 398L, 359L, 350L, 344L, 320L, 311L, 215L, 175L, 101L, 94L, 76L, 16L, 115L, 116L};
        List<Long> idList = Arrays.asList(ids);
        List<Theme> themes = themeRepository.findAllByIds(idList);
        return themes.stream().map(ThemeListResponse::new).collect(Collectors.toList());
    }

    public List<ThemeListResponse> getRecentThemes() {
        List<Theme> themes = themeRepository.findTop20ByOrderByRegistrationDateDesc();
        return themes.stream().map(ThemeListResponse::new).collect(Collectors.toList());
    }
};

