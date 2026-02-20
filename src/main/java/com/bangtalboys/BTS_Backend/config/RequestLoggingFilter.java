package com.bangtalboys.BTS_Backend.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

@Component
@Order(org.springframework.core.Ordered.HIGHEST_PRECEDENCE) // 가장 먼저 실행되도록 설정
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.info(">>>>>> Incoming request URI: {}", requestURI); // 들어오는 모든 요청 URI를 로그로 출력

        filterChain.doFilter(request, response);
    }
}
