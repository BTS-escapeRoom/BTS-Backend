package com.bangtalboys.BTS_Backend.oauth.authentication;

import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.utils.enums.Token;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public CustomSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        try {
            //OAuth2User
            CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

            String nickname = customUserDetails.getNickname();
            String socialType = customUserDetails.getSocialType();
            String socialId = customUserDetails.getSocialId();
            Long id = customUserDetails.getId();
            boolean isNewUser = customUserDetails.getIsNewUser();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();
            String role = auth.getAuthority();

            String refreshToken = jwtUtil.createJwt(Token.RefreshToken.getType(), id, socialType, socialId, role, Token.RefreshToken.getTtl());

//        response.addCookie(createCookie(Token.AccessToken.getType(), accessToken));
            response.addCookie(createCookie(Token.RefreshToken.getType(), refreshToken));
            String redirectUrl = "http://localhost:3000/oauth/login";

            if (isNewUser) {
                redirectUrl += "?result=signup";
            } else {
                if (nickname == null || nickname.trim().isEmpty()) {
                    redirectUrl += "?result=emptyNickname";
                } else {
                    redirectUrl += "?result=success";
                }
            }

            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            response.sendRedirect("https://bangtal-boys.com/oauth/login?result=fail");
        }
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}
