package com.bangtalboys.BTS_Backend.theme.controller;

import com.bangtalboys.BTS_Backend.theme.dto.ThemeResponse;
import com.bangtalboys.BTS_Backend.theme.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/themes")
public class ThemeController {
    private final ThemeService themeService;

    @GetMapping("")
    public ResponseEntity<List<ThemeResponse>> getThemes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer peoples,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer difficulty,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) Long cityId) {

        List<ThemeResponse> themeResponses = null;

        try {
            themeResponses = themeService.getThemes(title, peoples, genre, difficulty, districtId, cityId);
            return ResponseEntity.ok(themeResponses);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
