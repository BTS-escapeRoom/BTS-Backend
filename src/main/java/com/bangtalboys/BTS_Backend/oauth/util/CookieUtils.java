package com.bangtalboys.BTS_Backend.oauth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(name))
                    .findFirst();
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    // 객체 -> JSON 문자열 (직렬화)
    public static String serialize(Object obj) {
        try {
            String jsonStr = objectMapper.writeValueAsString(obj);
            return Base64.getUrlEncoder().encodeToString(jsonStr.getBytes());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize object to cookie value", e);
        }
    }

    // 쿠키 -> 객체 (역직렬화)
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        try {
            String base64Decoded = new String(Base64.getUrlDecoder().decode(cookie.getValue()));
            return objectMapper.readValue(base64Decoded, cls);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize cookie value", e);
        }
    }
}

