package com.bangtalboys.BTS_Backend.oauth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

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

    // Byte 배열을 Base64 문자열로 변환 (직렬화 시)
    public static String serialize(Object object) {
        try {
            // 1. ByteArrayOutputStream에 객체를 직렬화 (Java Serialization)
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bo);
            os.writeObject(object);
            os.close();

            // 2. 바이트 배열을 Base64로 인코딩하여 문자열로 반환
            return Base64.getUrlEncoder().encodeToString(bo.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }

    // Base64 문자열을 객체로 역직렬화 (역직렬화 시)
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        // 1. Base64 문자열을 바이트 배열로 디코딩
        byte[] bytes = Base64.getUrlDecoder().decode(cookie.getValue());
        try {
            // 2. ByteArrayInputStream에서 객체를 역직렬화
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));
            @SuppressWarnings("unchecked")
            T object = (T) is.readObject();
            is.close();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            // 기존의 IllegalArgumentException 대신 RuntimeException을 던지도록 수정
            throw new IllegalArgumentException("Failed to deserialize cookie value: " + cookie.getValue(), e);
        }
    }
}

