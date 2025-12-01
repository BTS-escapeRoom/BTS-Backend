package com.bangtalboys.BTS_Backend.config;

import com.bangtalboys.BTS_Backend.oauth.authentication.*;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtFilter;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.oauth.service.CustomOAuth2UserService;
import com.bangtalboys.BTS_Backend.oauth.util.AppleJwtUtils;

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
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import static com.bangtalboys.BTS_Backend.config.PermitAllPaths.PATHS;
import static com.bangtalboys.BTS_Backend.config.PermitAllPaths.SWAGGER_PATHS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomFailureHandler customFailureHandler;
    private final JwtUtil jwtUtil;
    private final AppleJwtUtils appleJwtUtils;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, 
                            CustomFailureHandler customFailureHandler, 
                            JwtUtil jwtUtil, 
                            AppleJwtUtils appleJwtUtils) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customFailureHandler = customFailureHandler;
        this.jwtUtil = jwtUtil;
        this.appleJwtUtils = appleJwtUtils;
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
        DefaultAuthorizationCodeTokenResponseClient defaultClient = new DefaultAuthorizationCodeTokenResponseClient();

        return request -> {
            // 1. 요청이 "apple"인지 확인
            if ("apple".equals(request.getClientRegistration().getRegistrationId())) {
                
                // 2. AppleJwtUtils를 이용해 실시간으로 유효한 JWT 생성
                String clientSecret = appleJwtUtils.createClientSecret();

                // 3. 기존 ClientRegistration 설정을 복사하되, clientSecret만 생성한 JWT로 교체
                ClientRegistration newRegistration = ClientRegistration
                        .withClientRegistration(request.getClientRegistration())
                        .clientSecret(clientSecret) 
                        .build();

                // 4. 교체된 Registration 정보를 가진 새로운 요청 객체 생성
                request = new OAuth2AuthorizationCodeGrantRequest(
                        newRegistration,
                        request.getAuthorizationExchange()
                );
            }

            // 5. (애플은 교체된 정보로, 나머지는 원래 정보로) 토큰 요청 전송
            return defaultClient.getTokenResponse(request);
        };
    }


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
                .userInfoEndpoint(userInfoEndpointConfig ->
                        userInfoEndpointConfig
                        .userService(customOAuth2UserService)
                        .oidcUserService((OAuth2UserService) customOAuth2UserService)
                    )
                .successHandler(customSuccessHandler)
                .failureHandler(customFailureHandler)

                .tokenEndpoint(tokenEndpointConfig -> 
                    tokenEndpointConfig.accessTokenResponseClient(accessTokenResponseClient())
            )
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
