package com.bangtalboys.BTS_Backend.theme.controller;

import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeListResponse;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeResponse;
import com.bangtalboys.BTS_Backend.theme.service.ThemeService;
import com.bangtalboys.BTS_Backend.utils.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="테마 API")
@RequestMapping("/v1/themes")
public class ThemeController {
    private final ThemeService themeService;
    private final JwtUtil jwtUtil;

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

    @Operation(summary = "테마 찜 등록/취소 (토글)")
    @PostMapping("/like")
    public ResponseEntity<Response<String>> createThemeLike(
            @AuthenticationPrincipal CustomOAuth2User oauth2User,
            @RequestParam(required = false) Long themeId
    ) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(themeService.createThemeLike(memberId, themeId)));
    }

    @Operation(summary = "찜한 테마 조회")
    @GetMapping("/like")
    public ResponseEntity<Response<List<ThemeListResponse>>> getMemberLikeThemes(
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(themeService.getLikeTheme(memberId)));
    }

    @Operation(summary = "랜덤 테마 조회 (빅배너용)")
    @GetMapping("/random")
    public ResponseEntity<Response<List<ThemeListResponse>>> getRandomThemes() {
        return ResponseEntity.ok(Response.ok(themeService.getRandomThemes()));
    }

    @Operation(summary = "인기 테마 조회 (밴드용)")
    @GetMapping("/popular")
    public ResponseEntity<Response<List<ThemeListResponse>>> getPopularThemes() {
        return ResponseEntity.ok(Response.ok(themeService.getPopularThemes()));
    }

    @Operation(summary = "최신 테마 조회 (밴드용)")
    @GetMapping("/recent")
    public ResponseEntity<Response<List<ThemeListResponse>>> getRecentThemes() {
        return ResponseEntity.ok(Response.ok(themeService.getRecentThemes()));
    }
}
