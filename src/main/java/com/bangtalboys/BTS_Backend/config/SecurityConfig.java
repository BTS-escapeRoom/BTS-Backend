package com.bangtalboys.BTS_Backend.config;

import com.bangtalboys.BTS_Backend.oauth.authentication.*;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtFilter;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 모든 Origin 허용
        configuration.setAllowedOriginPatterns(List.of("*")); // or use setAllowedOrigins(List.of("*"))

        configuration.setAllowedMethods(List.of("OPTIONS", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(false); // 쿠키 등 인증 정보는 안 보냄
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Set-Cookie")); // 필요 시
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository,
                                           AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo,
                                           CustomSuccessHandler customSuccessHandler) throws Exception {

        // CORS 설정
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
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
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // OPTIONS 요청 허용
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
