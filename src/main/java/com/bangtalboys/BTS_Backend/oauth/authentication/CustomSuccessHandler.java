package com.bangtalboys.BTS_Backend.oauth.authentication;

import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.oauth.service.OAuth2MemberService;
import com.bangtalboys.BTS_Backend.oauth.util.CookieUtils;
import com.bangtalboys.BTS_Backend.oauth.util.UrlUtils;

import com.bangtalboys.BTS_Backend.utils.enums.Token;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UrlUtils urlUtils;
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo;
    private final OAuth2MemberService oAuth2MemberService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    private static final String DEFAULT_REDIRECT_URL = "/";

    // 생성자 주입
    public CustomSuccessHandler(UrlUtils urlUtils,
                                JwtUtil jwtUtil,
                                AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo,
                                OAuth2AuthorizedClientService authorizedClientService,
                                OAuth2MemberService oAuth2MemberService) {
        this.urlUtils = urlUtils;
        this.jwtUtil = jwtUtil;
        this.authRequestRepo = authRequestRepo;
        this.authorizedClientService = authorizedClientService;
        this.oAuth2MemberService = oAuth2MemberService;
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
                if (urlUtils.isSafeReturnUrl(decodedUrl)) {
                    returnUrl = decodedUrl;
                }
            }
        }

        try {
            CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

            // 네이버인 경우 액세스 토큰, 리프레시 토큰 저장
            if ("naver".equals(customUserDetails.getSocialType())) {
                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                        oauthToken.getAuthorizedClientRegistrationId(),
                        oauthToken.getName()
                );

                String naverRefreshToken = authorizedClient.getRefreshToken() != null
                        ? authorizedClient.getRefreshToken().getTokenValue()
                        : null;

                oAuth2MemberService.updateNaverTokens(customUserDetails.getId(), naverRefreshToken);
            }

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

            CookieUtils.addCookie(response, Token.RefreshToken.getType(), refreshToken, 24*60*60);

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
            response.sendRedirect("https://www.bangtal-boys.com/oauth/login?result=fail");
        }
    }

}

