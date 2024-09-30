package com.bangtalboys.BTS_Backend.config;

import com.bangtalboys.BTS_Backend.utils.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // 세션 사용 안함
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 회원가입, 로그인 관련 API는 Jwt 인증 없이 접근 가능
                .antMatchers("/api/auth/**").permitAll()
                // 나머지 모든 API는 Jwt 인증 필요
                .anyRequest().authenticated()
                .and()
                // Http 요청에 대한 Jwt 유효성 선 검사
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}