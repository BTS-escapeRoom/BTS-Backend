package com.bangtalboys.BTS_Backend.city.controller;

import com.bangtalboys.BTS_Backend.city.dto.CityResponse;
import com.bangtalboys.BTS_Backend.city.service.CityService;
import com.bangtalboys.BTS_Backend.config.error.exception.BusinessBaseException;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
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
@Tag(name="지역 API")
@RequestMapping("/v1/cities")
public class CityController {
    private final CityService cityService;

    @Operation(summary = "지역 리스트 조회", description = "지역과 세부지역 전달")
    @GetMapping("")
    public ResponseEntity<Response<List<CityResponse>>> getAllCity() {

        try {
            List<CityResponse> cityResponses = cityService.getAllCity();
            return ResponseEntity.ok(Response.ok(cityResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error(e.getMessage(), "500"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<CityResponse>> getCity(
            @PathVariable Long id
    ) {

        CityResponse cityResponse = null;

        try {
            cityResponse = cityService.getOneCity(id);
            return ResponseEntity.ok(Response.ok(cityResponse));
        } catch (BusinessBaseException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus())
                    .body(Response.error(e.getMessage(), e.getErrorCode().getCode()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Internal Server Error", "500"));
        }
    }
}
