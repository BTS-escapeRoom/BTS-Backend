package com.bangtalboys.BTS_Backend.theme.controller;

import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeListRequest;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeListPageResponse;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeListResponse;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeResponse;
import com.bangtalboys.BTS_Backend.theme.service.ThemeService;
import com.bangtalboys.BTS_Backend.utils.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "테마 리스트 조회", description = "제목, 가게, 사람수, 난이도, 장르, 지역으로 필터링하여 전달")
    @GetMapping("")
    public ResponseEntity<Response<ThemeListPageResponse>> getAllTheme(
            @RequestParam(required = false) @Parameter(description = "검색할 키워드") String keyword,
            @RequestParam(required = false) @Parameter(description = "인원수") Integer peoples,
            @RequestParam(required = false) @Parameter(description = "최소 난이도") Integer minDiff,
            @RequestParam(required = false) @Parameter(description = "최대 난이도") Integer maxDiff,
            @RequestParam(required = false) @Parameter(description = "장르 ID 리스트") List<Long> genreIdList,
            @RequestParam(required = false) @Parameter(description = "구 ID 리스트") List<Long> districtIdList,
            @RequestParam(required = false) @Parameter(description = "시 ID 리스트") List<Long> cityIdList,
            @RequestParam(required = false) @Parameter(description = "정렬 기준 (recent, popular, distance)") String sort,
            @RequestParam(required = false) @Parameter(description = "위도 (거리 정렬 시 필요)") Double latitude,
            @RequestParam(required = false) @Parameter(description = "경도 (거리 정렬 시 필요)") Double longitude,
            @Valid @RequestParam(required = false) @Parameter(description = "페이지",  example = "1") Integer page
    ) {

        if (page != null && page < 1) {
            throw new IllegalArgumentException("page는 1 이상이어야 합니다.");
        }

        ThemeListRequest themeListRequest = new ThemeListRequest(
                keyword, peoples, minDiff, maxDiff, genreIdList, districtIdList, cityIdList, sort, latitude, longitude, page);

        return ResponseEntity.ok(Response.ok(themeService.getAllTheme(themeListRequest)));
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

    @Operation(summary = "내가 찜한 테마 조회")
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

    @Operation(summary = "실시간 인기 테마 조회 (밴드용)")
    @GetMapping("/popular/realtime")
    public ResponseEntity<Response<List<ThemeListResponse>>> getPopularRealtimeThemes() {
        return ResponseEntity.ok(Response.ok(themeService.getRealtimePopularThemes()));
    }

    @Operation(summary = "좋아요 개수 높은 순 (밴드용)")
    @GetMapping("/most-liked")
    public ResponseEntity<Response<List<ThemeListResponse>>> getMostLikedThemes() {
        return ResponseEntity.ok(Response.ok(themeService.getMostLikedThemes()));
    }
}
