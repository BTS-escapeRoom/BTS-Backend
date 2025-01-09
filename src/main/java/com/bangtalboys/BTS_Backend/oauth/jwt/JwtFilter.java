package com.bangtalboys.BTS_Backend.oauth.jwt;

import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import com.bangtalboys.BTS_Backend.oauth.dto.UserDto;
import com.bangtalboys.BTS_Backend.utils.enums.Role;
import com.bangtalboys.BTS_Backend.utils.enums.Token;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
//        String authorization = null;
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("Authorization")) {
//                authorization = cookie.getValue();
//            }
//        }


        //Authorization 헤더 검증
//        if (authorization == null) {
//            System.out.println("token null");
//            filterChain.doFilter(request, response);
//            return;
//        }


        //Authorization 헤더 검증
//        if (authorization == null) {
//            System.out.println("token null");
//            filterChain.doFilter(request, response);
//            return;
//        }

//        //토큰
//        String token = authorization;
//
//        //토큰 소멸 시간 검증
//        if (jwtUtil.isExpired(token)) {
//            System.out.println("token expired");
//            filterChain.doFilter(request, response);
//            return;
//        }
//

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String token = request.getHeader("Authorization");

//        if (Objects.equals(token, "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6IktBS0FPMzc2MTM3OTU4NSIsImlkIjoiNCIsInJvbGUiOiJSb2xlLlJPTEVfVVNFUiIsImlhdCI6MTczMDM0OTQ0MSwiZXhwIjoxNzMwMzQ5NjU3fQ.otPoIAdm3G-bI83u-o0MOX5Pm8CRxDNd0B_u8witre8")) {
//            //userDTO를 생성하여 값 set
//            UserDto userDto = UserDto.builder()
//                    .build();
//
//            //UserDetails에 회원 정보 객체 담기
//            CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
//
//            //스프링 시큐리티 인증 토큰 생성
//            Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
//            //세션에 사용자 등록
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//
//            filterChain.doFilter(request, response);

//        }
        // 권한 체크 없는 기능일 경우
        if (token == null || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        token = token.substring(7);

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code(상태반환: 다음 필터로 넘기지 않고 바로 응답 반환)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String tokenType = jwtUtil.getType(token);
        if (!tokenType.equals(Token.AccessToken.getType())) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code(상태반환: 다음 필터로 넘기지 않고 바로 응답 반환)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
            return;
        }

        //토큰에서 id, username과 role 획득
        Long id = jwtUtil.getId(token);
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        //userDTO를 생성하여 값 set
        UserDto userDto = UserDto.builder()
                .id(id)
                .username(username)
                .role(Role.valueOf(role))
                .build();

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}