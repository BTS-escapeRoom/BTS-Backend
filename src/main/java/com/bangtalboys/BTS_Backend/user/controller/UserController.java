package com.bangtalboys.BTS_Backend.user.controller;

import com.bangtalboys.BTS_Backend.user.oauth.dto.AuthToken;
import com.bangtalboys.BTS_Backend.user.oauth.dto.KakaoLoginRequest;
import com.bangtalboys.BTS_Backend.user.oauth.dto.LoginRequest;
import com.bangtalboys.BTS_Backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login/kakao")
    public ResponseEntity<AuthToken> login(@RequestBody KakaoLoginRequest params) {
        return ResponseEntity.ok(userService.login(params));
    }

}