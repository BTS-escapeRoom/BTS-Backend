package com.bangtalboys.BTS_Backend.config;

import com.bangtalboys.BTS_Backend.oauth.authentication.*;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtFilter;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomFailureHandler customFailureHandler;
    private final JwtUtil jwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomFailureHandler customFailureHandler, JwtUtil jwtUtil) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customFailureHandler = customFailureHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public CustomSuccessHandler customSuccessHandler(AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo) {
        return new CustomSuccessHandler(jwtUtil, authRequestRepo);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository,
                                           AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo,
                                           CustomSuccessHandler customSuccessHandler) throws Exception {

        // CORS 설정
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {

                    CorsConfiguration configuration = new CorsConfiguration();

                    // 허용할 출처
                    configuration.setAllowedOrigins(List.of(
                            "http://localhost:3000", // 개발 환경
                            "http://localhost:8080",
                            "https://apis.bangtal-boys.com" // 배포 환경
                    ));
                    // 허용할 HTTP 메서드
                    configuration.setAllowedMethods(List.of("OPTIONS", "GET", "POST", "PUT", "DELETE"));
                    // 인증 정보 허용 (쿠키 등)
                    configuration.setAllowCredentials(true);
                    // 허용할 헤더
                    configuration.setAllowedHeaders(List.of("*"));
                    // Preflight 요청 캐싱 시간 (초)
                    configuration.setMaxAge(3600L);

                    return configuration;
                }));

        // CSRF 비활성화
        http.csrf((auth) -> auth.disable());

        // Form 로그인 방식 비활성화
        http.formLogin((auth) -> auth.disable());

        // HTTP Basic 인증 방식 비활성화
        http.httpBasic((auth) -> auth.disable());

        // JWT 필터 추가
        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // OAuth2 로그인 설정
        http.oauth2Login((oauth2) -> oauth2
                .authorizationEndpoint(authorizationEndpointConfig ->
                        authorizationEndpointConfig
                                .authorizationRequestResolver(
                                        new CustomAuthorizationRequestResolver(clientRegistrationRepository)
                                )
                                .authorizationRequestRepository(authRequestRepo)
                )
                .userInfoEndpoint(userInfoEndpointConfig ->
                        userInfoEndpointConfig.userService(customOAuth2UserService))
                .successHandler(customSuccessHandler)
                .failureHandler(customFailureHandler)
        );

        // 경로별 인가 설정
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/check-signup").permitAll()
                .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/reissue").permitAll()
                .anyRequest().authenticated()
        );

        // 세션 상태를 STATELESS로 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }
}
