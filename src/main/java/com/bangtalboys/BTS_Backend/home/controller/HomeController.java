package com.bangtalboys.BTS_Backend.home.controller;

import com.bangtalboys.BTS_Backend.home.service.HomeService;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.utils.enums.Role;
import com.bangtalboys.BTS_Backend.utils.enums.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/home")
public class HomeController {
    private final HomeService homeService;
    private final JwtUtil jwtUtil;

    @GetMapping("")
    public String home() {
        return homeService.getHome();

    }
}
