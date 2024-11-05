package com.bangtalboys.BTS_Backend.city.controller;

import com.bangtalboys.BTS_Backend.city.dto.CityResponse;
import com.bangtalboys.BTS_Backend.city.service.CityService;
import com.bangtalboys.BTS_Backend.config.error.exception.BusinessBaseException;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.utils.Response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cities")
public class CityController {
    private final CityService cityService;

    @GetMapping("")
    public ResponseEntity<Response<List<CityResponse>>> getAllCity() {

        try {
            List<CityResponse> cityResponses = cityService.getAllCity();
            return ResponseEntity.ok(Response.ok(cityResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("", "500"));
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Response<CityResponse>> getCity(
//            @PathVariable Long id
//    ) {
//
//        CityResponse cityResponse = null;
//
//        try {
//            cityResponse = cityService.getOneCity(id);
//            return ResponseEntity.ok(Response.ok(cityResponse));
//        } catch (BusinessBaseException e) {
//            return ResponseEntity.status(e.getErrorCode().getStatus())
//                    .body(Response.error(e.getMessage(), e.getErrorCode().getCode()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Response.error("Internal Server Error", "500"));
//        }
//    }
}
