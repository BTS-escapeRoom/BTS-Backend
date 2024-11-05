package com.bangtalboys.BTS_Backend.theme.controller;

import com.bangtalboys.BTS_Backend.config.error.exception.BusinessBaseException;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeListResponse;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeResponse;
import com.bangtalboys.BTS_Backend.theme.service.ThemeService;
import com.bangtalboys.BTS_Backend.utils.Response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/themes")
public class ThemeController {
    private final ThemeService themeService;

    @GetMapping("")
    public ResponseEntity<Response<List<ThemeListResponse>>> getAllTheme(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer peoples,
            @RequestParam(required = false) Integer difficulty,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) Long cityId) {

        try {
            List<ThemeListResponse> themeListResponses = themeService.getAllTheme(title, peoples, difficulty, genreId, districtId, cityId);
            return ResponseEntity.ok(Response.ok(themeListResponses));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error(e.getMessage(), "500"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ThemeResponse>> getTheme(
            @PathVariable Long id
    ) {


        try {
            ThemeResponse themeResponses = themeService.getOneTheme(id);
            return ResponseEntity.ok(Response.ok(themeResponses));
        } catch (BusinessBaseException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus())
                    .body(Response.error(e.getMessage(), e.getErrorCode().getCode()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Internal Server Error", "500"));
        }
    }
}
