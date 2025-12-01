package com.bangtalboys.BTS_Backend.oauth.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

@Component
@Slf4j
public class CustomFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.error("🔥 인증 실패 최상위 메시지: {}", exception.getMessage());

        // 1. OAuth2AuthenticationException인지 확인하여 상세 에러 뜯어보기
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oauthException = (OAuth2AuthenticationException) exception;
            OAuth2Error error = oauthException.getError();
            
            log.error("--------------------------------------------------");
            log.error("🚨 OAuth2 Error Code : {}", error.getErrorCode());
            log.error("🚨 OAuth2 Description: {}", error.getDescription());
            log.error("🚨 OAuth2 Error URI  : {}", error.getUri());
            log.error("--------------------------------------------------");
        }

        // 2. 근본 원인(Cause)이 있다면 출력
        if (exception.getCause() != null) {
            log.error("🚨 Root Cause: ", exception.getCause());
        }
        // 실패 시 리다이렉트 URL 지정
        response.sendRedirect("http://localhost:3000/oauth/login?result=error");
    }
}

