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
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo;

    private static final String DEFAULT_REDIRECT_URL = "/";

    // 생성자 주입
    public CustomSuccessHandler(JwtUtil jwtUtil,
                                AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo) {
        this.jwtUtil = jwtUtil;
        this.authRequestRepo = authRequestRepo;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // returnUrl 검증
        OAuth2AuthorizationRequest authRequest = authRequestRepo.removeAuthorizationRequest(request, response);

        String returnUrl = DEFAULT_REDIRECT_URL;

        if (authRequest != null) {
            Object returnUrlObj = authRequest.getAdditionalParameters().get("return-url");

            if (returnUrlObj instanceof String) {
                String decodedUrl = (String) returnUrlObj;
                if (isSafeReturnUrl(decodedUrl)) {
                    returnUrl = decodedUrl;
                }
            }
        }

        try {
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

            Cookie myCookie = createCookie(Token.RefreshToken.getType(), refreshToken);
            addSameSiteCookie(response, myCookie);

            // returnUrl이 이미 쿼리 파라미터를 포함하는지 확인
            String delimiter = returnUrl.contains("?") ? "&" : "?";
            String resultParam;

            if (isNewUser) {
                resultParam = "result=signup";
            } else if (nickname == null || nickname.trim().isEmpty()) {
                resultParam = "result=emptyNickname";
            } else {
                resultParam = "result=success";
            }

            String redirectUrl = returnUrl + delimiter + resultParam;
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            System.out.println("🔥 예외 발생: " + e.getMessage());
            // 인증 중 예외가 발생했을 경우 fallback URL로 리다이렉트
            response.sendRedirect("http://localhost:3000/oauth/login?result=fail");
        }
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

    private boolean isSafeReturnUrl(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();

            // 로컬 개발 환경 허용 (선택)
            if (host == null) return false;

            return host.endsWith("bangtal-boys.com") || host.equals("localhost");
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public String getDomain(String input) {
        int index = input.indexOf('?');
        if (index == -1) {
            return input;
        }
        return input.substring(0, index);
    }
}

