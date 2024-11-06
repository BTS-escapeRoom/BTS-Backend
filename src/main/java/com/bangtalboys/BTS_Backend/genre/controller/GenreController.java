package com.bangtalboys.BTS_Backend.genre.controller;

import com.bangtalboys.BTS_Backend.genre.dto.GenreResponse;
import com.bangtalboys.BTS_Backend.genre.service.GenreService;
import com.bangtalboys.BTS_Backend.utils.Response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="장르 API")
@RequestMapping("v1/genres")
public class GenreController {

    private final GenreService genreService;

    @Operation(summary = "장르 리스트 조회")
    @GetMapping("")
    public ResponseEntity<Response<List<GenreResponse>>> getAllGenre() {

        try {
            List<GenreResponse> genreResponses = genreService.getAllGenre();
            return ResponseEntity.ok(Response.ok(genreResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error(e.getMessage(), "500"));
        }
    }
}
