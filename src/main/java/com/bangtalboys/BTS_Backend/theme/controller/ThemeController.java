package com.bangtalboys.BTS_Backend.theme.controller;

import com.bangtalboys.BTS_Backend.config.error.exception.BusinessBaseException;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeListResponse;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeResponse;
import com.bangtalboys.BTS_Backend.theme.service.ThemeService;
import com.bangtalboys.BTS_Backend.utils.Response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="테마 API")
@RequestMapping("/v1/themes")
public class ThemeController {
    private final ThemeService themeService;

    @Operation(summary = "테마 리스트 조회", description = "제목, 사람수, 난이도, 장르, 지역으로 필터링하여 전달")
    @GetMapping("")
    public ResponseEntity<Response<List<ThemeListResponse>>> getAllTheme(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer peoples,
            @RequestParam(required = false) Integer difficulty,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) Long cityId
    ) {

        return ResponseEntity.ok(Response.ok(themeService.getAllTheme(title, peoples, difficulty, genreId, districtId, cityId)));
    }

    @Operation(summary = "테마 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<Response<ThemeResponse>> getTheme(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(Response.ok(themeService.getOneTheme(id)));
    }
}
