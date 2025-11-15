package com.bangtalboys.BTS_Backend.oauth.controller;

import com.bangtalboys.BTS_Backend.oauth.dto.AppSocialLoginRequest;
import com.bangtalboys.BTS_Backend.oauth.dto.AuthResponse;
import com.bangtalboys.BTS_Backend.oauth.service.AppAuthService;
import com.bangtalboys.BTS_Backend.utils.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API", description = "소셜 로그인 등")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AppAuthService appAuthService;

    @Operation(summary = "앱 소셜 로그인", description = "code를 받아 로그인 처리 후 JWT 발급")
    @PostMapping("/login/{provider}")
    public ResponseEntity<Response<AuthResponse>> appSocialLogin(
            @PathVariable("provider") String provider,
            @RequestBody @Valid AppSocialLoginRequest req
    ) {
        return ResponseEntity.ok(Response.ok(appAuthService.socialLogin(provider, req)));
    }
}
