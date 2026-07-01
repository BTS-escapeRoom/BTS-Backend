package com.bangtalboys.BTS_Backend.config;

import com.bangtalboys.BTS_Backend.oauth.authentication.CustomAuthorizationRequestResolver;
import com.bangtalboys.BTS_Backend.oauth.authentication.CustomFailureHandler;
import com.bangtalboys.BTS_Backend.oauth.authentication.CustomSuccessHandler;
import com.bangtalboys.BTS_Backend.oauth.authentication.HttpCookieOAuth2AuthorizationRequestRepository;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtFilter;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.oauth.service.CustomOAuth2UserService;
import com.bangtalboys.BTS_Backend.oauth.service.OAuth2MemberService;
import com.bangtalboys.BTS_Backend.oauth.util.AppleJwtUtils;
import com.bangtalboys.BTS_Backend.oauth.util.UrlUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.bangtalboys.BTS_Backend.config.PermitAllPaths.PATHS;
import static com.bangtalboys.BTS_Backend.config.PermitAllPaths.SWAGGER_PATHS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UrlUtils urlUtils;
    private final JwtUtil jwtUtil;
    private final AppleJwtUtils appleJwtUtils;

    public SecurityConfig(
            CustomOAuth2UserService customOAuth2UserService,
            UrlUtils urlUtils,
            JwtUtil jwtUtil,
            AppleJwtUtils appleJwtUtils
    ) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.urlUtils = urlUtils;
        this.jwtUtil = jwtUtil;
        this.appleJwtUtils = appleJwtUtils;
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(
            ClientRegistrationRepository clientRegistrationRepository
    ) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public CustomSuccessHandler customSuccessHandler(
            AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo,
            OAuth2AuthorizedClientService authorizedClientService,
            OAuth2MemberService oAuth2MemberService
    ) {
        return new CustomSuccessHandler(
                urlUtils,
                jwtUtil,
                authRequestRepo,
                authorizedClientService,
                oAuth2MemberService
        );
    }

    @Bean
    public CustomFailureHandler customFailureHandler(
            AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo
    ) {
        return new CustomFailureHandler(urlUtils, authRequestRepo);
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient defaultClient = new DefaultAuthorizationCodeTokenResponseClient();

        return request -> {
            if ("apple".equals(request.getClientRegistration().getRegistrationId())) {
                String clientSecret = appleJwtUtils.createClientSecret();

                ClientRegistration newRegistration = ClientRegistration
                        .withClientRegistration(request.getClientRegistration())
                        .clientSecret(clientSecret)
                        .build();

                request = new OAuth2AuthorizationCodeGrantRequest(
                        newRegistration,
                        request.getAuthorizationExchange()
                );
            }

            return defaultClient.getTokenResponse(request);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            ClientRegistrationRepository clientRegistrationRepository,
            AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo,
            CustomSuccessHandler customSuccessHandler,
            CustomFailureHandler customFailureHandler
    ) throws Exception {

        // CORS 설정
        // http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // CSRF 비활성화
        http.csrf(auth -> auth.disable());

        // Form 로그인 방식 비활성화
        http.formLogin(auth -> auth.disable());

        // HTTP Basic 인증 방식 비활성화
        http.httpBasic(auth -> auth.disable());

        // JWT 필터 추가
        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
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

        // 인증 실패시 302 -> 401 Unauthorized 응답 처리
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
        );

        // 경로별 인가 설정
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

            // PaaS health check (GET + HEAD)
            auth.requestMatchers(HttpMethod.GET, "/health").permitAll();
            auth.requestMatchers(HttpMethod.HEAD, "/health").permitAll();

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
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }
}