package com.bangtalboys.BTS_Backend.oauth.controller;

import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.dto.MemberResponse;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.member.service.MemberService;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.utils.enums.Status;
import com.bangtalboys.BTS_Backend.utils.enums.Token;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Tag(name="REISSUE API")
public class ReissueController {
    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    public ReissueController(JwtUtil jwtUtil, MemberService memberService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
    }

    @Operation(
            summary = "토큰 재발급",
            description = "웹: 쿠키 전송 | 앱: Authorization 헤더로 전송 (Bearer {refreshToken})"
    )
    @Parameters({
            @Parameter(
                    name = "Cookie",
                    description = "웹 전용 - refresh={token}",
                    in = ParameterIn.COOKIE
            ),
            @Parameter(
                    name = "Authorization",
                    description = "앱 전용 - Bearer {refreshToken}",
                    in = ParameterIn.HEADER
            )
    })
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {  // null 체크 추가
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Token.RefreshToken.getType())) {
                    refresh = cookie.getValue();
                }
            }
        }

        if (refresh == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                refresh = authHeader.substring(7);
            }
        }

        if (refresh == null) {
            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getType(refresh);
        if (!category.equals(Token.RefreshToken.getType())) {
            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        Long id = jwtUtil.getId(refresh);
        String socialType = jwtUtil.getSocialType(refresh);
        String socialId = jwtUtil.getSocialId(refresh);
        String role = jwtUtil.getRole(refresh);

        // 탈퇴한 유저인지 판단
        try {
            MemberResponse memberRes = memberService.getOneMember(id);

            // 탈퇴 상태(INACTIVE)인 경우 처리
            if (memberRes.getStatus() == Status.INACTIVE) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("user deleted");
            }
        } catch (NotFoundException e) {
            // DB에 유저가 아예 없는 경우 (Hard Delete 되었거나 존재하지 않는 유저)
            // 전역 예외 처리기로 가기 전에 캐치해서 401로 응답을 통일해 줍니다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("user not found");
        }

        //make new JWT
        String newAccess = jwtUtil.createJwt(Token.AccessToken.getType(), id, socialType, socialId, role, 600000L);
        String newRefresh = jwtUtil.createJwt(Token.RefreshToken.getType(), id, socialType, socialId, role, 86400000L);

        //response
        response.setHeader(Token.AccessToken.getType(), newAccess);
        response.setHeader(Token.RefreshToken.getType(), newRefresh); // 앱용 헤더 응답 추가
        Cookie refreshCookie = createCookie(Token.RefreshToken.getType(), newRefresh); // 웹용 쿠키 응답 추가
        addSameSiteCookie(response, refreshCookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain("bangtal-boys.com");

        return cookie;
    }

    private void addSameSiteCookie(HttpServletResponse response, Cookie cookie) {
        String cookieStr = String.format("%s=%s; Max-Age=%d; Path=%s; Secure; HttpOnly; SameSite=None; Domain=%s",
                cookie.getName(), cookie.getValue(), cookie.getMaxAge(), cookie.getPath(), cookie.getDomain());

        response.addHeader("Set-Cookie", cookieStr);
    }

    @Data
    public static class ReissueResponse {
        private final String accessToken;
    }
}

