package com.bangtalboys.BTS_Backend.oauth.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        System.out.println("🔥 인증 실패: " + exception.getMessage());
        // 실패 시 리다이렉트 URL 지정
        response.sendRedirect("http://localhost:3000/oauth/login?result=error");
    }
}

