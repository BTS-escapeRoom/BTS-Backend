package com.bangtalboys.BTS_Backend.oauth.jwt;

import com.bangtalboys.BTS_Backend.utils.enums.Token;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@Hidden
public class ReissueController {
    private final JwtUtil jwtUtil;
    public ReissueController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(Token.RefreshToken.getType())) {
                refresh = cookie.getValue();
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

        //make new JWT
        String newAccess = jwtUtil.createJwt(Token.AccessToken.getType(), id, socialType, socialId, role, 600000L);
        String newRefresh = jwtUtil.createJwt(Token.RefreshToken.getType(), id, socialType, socialId, role, 86400000L);

        //response
        response.setHeader(Token.AccessToken.getType(), newAccess);
        Cookie myCookie = createCookie(Token.RefreshToken.getType(), newRefresh);
        addSameSiteCookie(response, myCookie);

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
}