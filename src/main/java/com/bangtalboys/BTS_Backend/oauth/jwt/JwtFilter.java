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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

import static com.bangtalboys.BTS_Backend.config.PermitAllPaths.PATHS;
import static com.bangtalboys.BTS_Backend.config.PermitAllPaths.SWAGGER_PATHS;

public class JwtFilter extends OncePerRequestFilter {

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private final JwtUtil jwtUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain) 
        throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (isPermitAllPath(path, method) || "OPTIONS".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String token = request.getHeader("Authorization");

        // 권한 체크 없는 기능일 경우
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        token = token.substring(7);

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("access token expired");
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String tokenType = jwtUtil.getType(token);
        if (!tokenType.equals(Token.AccessToken.getType())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("invalid access token");
            return;
        }

        //userDTO를 생성하여 값 set
        UserDto userDto = UserDto.builder()
                .id(jwtUtil.getId(token))
                .socialType(jwtUtil.getSocialType(token))
                .socialId(jwtUtil.getSocialId(token))
                .role(jwtUtil.getRole(token))
                .build();

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private boolean isPermitAllPath(String requestPath, String requestMethod) {
        for (PermitAllPaths.PermitPath p : PATHS) {
            boolean methodMatches = (p.getMethod() == null || p.getMethod().name().equalsIgnoreCase(requestMethod));
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