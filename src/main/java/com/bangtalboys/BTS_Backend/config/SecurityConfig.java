package com.bangtalboys.BTS_Backend.config;

import com.bangtalboys.BTS_Backend.oauth.authentication.*;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtFilter;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.oauth.util.AppleJwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static com.bangtalboys.BTS_Backend.config.PermitAllPaths.PATHS;
import static com.bangtalboys.BTS_Backend.config.PermitAllPaths.SWAGGER_PATHS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomFailureHandler customFailureHandler;
    private final JwtUtil jwtUtil;
    private final AppleJwtUtil appleJwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomFailureHandler customFailureHandler, JwtUtil jwtUtil, AppleJwtUtil appleJwtUtil) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customFailureHandler = customFailureHandler;
        this.jwtUtil = jwtUtil;
        this.appleJwtUtil = appleJwtUtil;
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
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
        
        client.setRequestEntityConverter(new CustomRequestEntityConverter(appleJwtUtil));
        
        return client;
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.setAllowedOriginPatterns(List.of(
//                "http://localhost:3000",
//                "http://localhost:8080",
//                "https://bangtal-boys.com"
//        ));
//        configuration.setAllowedMethods(List.of("OPTIONS", "GET", "POST", "PUT", "DELETE"));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(List.of("*"));
//
//
//        configuration.setExposedHeaders(List.of("Set-Cookie")); // 필요 시
//        configuration.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository,
                                           AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo,
                                           CustomSuccessHandler customSuccessHandler) throws Exception {

        // CORS 설정
//        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

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
                .tokenEndpoint(token -> token
                    .accessTokenResponseClient(accessTokenResponseClient())
                )
                .userInfoEndpoint(userInfoEndpointConfig ->
                        userInfoEndpointConfig.userService(customOAuth2UserService))
                .successHandler(customSuccessHandler)
                .failureHandler(customFailureHandler)
        );

        // 인증 실패시 302 -> 401 Unauthorized 응답 처리 추가
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
        );

        // 경로별 인가 설정
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

            for (PermitAllPaths.PermitPath p : PATHS) {
                if (p.getMethod() != null) {
                    auth.requestMatchers(p.getMethod(), p.getPattern()).permitAll();
                } else {
                    auth.requestMatchers(p.getPattern()).permitAll();
                }
            }

            for (String swagger : SWAGGER_PATHS) {
                auth.requestMatchers(swagger).permitAll();
            }

            auth.anyRequest().authenticated();
        });

        // 세션 상태를 STATELESS로 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }
}
