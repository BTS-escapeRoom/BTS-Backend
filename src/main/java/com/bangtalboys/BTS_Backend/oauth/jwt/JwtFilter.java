package com.bangtalboys.BTS_Backend.oauth.jwt;

import com.bangtalboys.BTS_Backend.config.PermitAllPaths;
import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import com.bangtalboys.BTS_Backend.oauth.dto.UserDto;
import com.bangtalboys.BTS_Backend.utils.enums.Token;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.bangtalboys.BTS_Backend.config.PermitAllPaths.PATHS;
import static com.bangtalboys.BTS_Backend.config.PermitAllPaths.SWAGGER_PATHS;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (isPermitAllPath(path, method) || "OPTIONS".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 Authorization 토큰을 꺼냄
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        token = token.substring(7);

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("access token expired");
            return;
        }

        // 토큰이 access token인지 확인
        String tokenType = jwtUtil.getType(token);
        if (!tokenType.equals(Token.AccessToken.getType())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("invalid access token");
            return;
        }

        UserDto userDto = UserDto.builder()
                .id(jwtUtil.getId(token))
                .socialType(jwtUtil.getSocialType(token))
                .socialId(jwtUtil.getSocialId(token))
                .role(jwtUtil.getRole(token))
                .build();

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customOAuth2User,
                null,
                customOAuth2User.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private boolean isPermitAllPath(String requestPath, String requestMethod) {
        // PaaS health check는 JWT 검사 없이 통과 (GET + HEAD)
        if (("GET".equalsIgnoreCase(requestMethod) || "HEAD".equalsIgnoreCase(requestMethod))
                && "/health".equals(requestPath)) {
            return true;
        }

        for (PermitAllPaths.PermitPath p : PATHS) {
            boolean methodMatches = p.getMethod() == null
                    || p.getMethod().name().equalsIgnoreCase(requestMethod);

            boolean pathMatches = pathMatcher.match(p.getPattern(), requestPath);

            if (methodMatches && pathMatches) {
                return true;
            }
        }

        for (String swaggerPattern : SWAGGER_PATHS) {
            if (pathMatcher.match(swaggerPattern, requestPath)) {
                return true;
            }
        }

        return false;
    }
}