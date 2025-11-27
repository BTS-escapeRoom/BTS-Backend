package com.bangtalboys.BTS_Backend.config;

import org.springframework.http.HttpMethod;

public final class PermitAllPaths {

    private PermitAllPaths() {}

    public static final PermitPath[] PATHS = {
            new PermitPath(HttpMethod.GET, "/v1/themes/**"),
            new PermitPath(HttpMethod.POST, "/v1/auth/login/**"),
            new PermitPath(null, "/check-signup"),  // null이면 메서드 무관
            new PermitPath(null, "/reissue"),
            new PermitPath(null, "/oauth2/**")
    };

    public static final String[] SWAGGER_PATHS = {
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    public static class PermitPath {
        private final HttpMethod method;  // null 허용
        private final String pattern;

        public PermitPath(HttpMethod method, String pattern) {
            this.method = method;
            this.pattern = pattern;
        }

        public HttpMethod getMethod() { return method; }
        public String getPattern() { return pattern; }
    }
}
