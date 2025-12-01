package com.bangtalboys.BTS_Backend.oauth.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import com.bangtalboys.BTS_Backend.oauth.util.UrlUtils;
import org.springframework.stereotype.Component;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.OAuth2Error;

@Component
@Slf4j
public class CustomFailureHandler implements AuthenticationFailureHandler {

    private final UrlUtils urlUtils;
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo;
    
    private static final String DEFAULT_FAILURE_URL = "http://localhost:3000/oauth/login";

    public CustomFailureHandler(UrlUtils urlUtils,
                                AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo) {
        this.urlUtils = urlUtils;
        this.authRequestRepo = authRequestRepo;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        
        // 1. 로그 출력
        log.error("🔥 인증 실패 최상위 메시지: {}", exception.getMessage());
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oauthException = (OAuth2AuthenticationException) exception;
            OAuth2Error error = oauthException.getError();
            log.error("🚨 OAuth2 Error Code : {}", error.getErrorCode());
            log.error("🚨 OAuth2 Description: {}", error.getDescription());
        }

        // 2. ✅ 저장된 요청 정보(쿠키 등)를 가져오면서 삭제
        OAuth2AuthorizationRequest authRequest = authRequestRepo.removeAuthorizationRequest(request, response);

        String targetUrl = DEFAULT_FAILURE_URL;

        // 3. ✅ return-url 파라미터 추출
        if (authRequest != null) {
            Object returnUrlObj = authRequest.getAdditionalParameters().get("return-url");
            if (returnUrlObj instanceof String) {
                String decodedUrl = (String) returnUrlObj;
                if (urlUtils.isSafeReturnUrl(decodedUrl)) {
                    targetUrl = decodedUrl;
                }
            }
        }

        // 4. ✅ 에러 파라미터 붙여서 리다이렉트
        // 이미 쿼리 파라미터가 있는지 확인하여 ? 또는 & 붙이기
        String delimiter = targetUrl.contains("?") ? "&" : "?";
        
        // 에러 원인을 쿼리 파라미터로 전달 (필요 시 URLEncoder.encode 사용)
        String redirectUrl = targetUrl + delimiter + "result=error&message=" + exception.getMessage();

        response.sendRedirect(redirectUrl);
    }
}