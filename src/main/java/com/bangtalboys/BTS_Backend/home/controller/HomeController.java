package com.bangtalboys.BTS_Backend.home.controller;

import com.bangtalboys.BTS_Backend.home.dto.response.HomeResponse;
import com.bangtalboys.BTS_Backend.home.service.HomeService;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.utils.enums.Role;
import com.bangtalboys.BTS_Backend.utils.enums.Token;
import com.bangtalboys.BTS_Backend.utils.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/home")
@Tag(name="메인 홈 API")
public class HomeController {
    private final HomeService homeService;
    private final JwtUtil jwtUtil;

    @GetMapping("")
    public ResponseEntity<Response<List<HomeResponse>>> home() {
        return ResponseEntity.ok(Response.ok(homeService.getHome()));
    }
}
