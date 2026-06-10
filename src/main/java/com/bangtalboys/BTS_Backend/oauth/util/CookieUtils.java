package com.bangtalboys.BTS_Backend.oauth.util;

import org.springframework.http.ResponseCookie;
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

import jakarta.servlet.http.Cookie;

public class CookieUtils {

    // [수정] 리턴 타입을 Optional<Cookie>로 변경 (기존 코드에선 Optional<ResponseCookie>였으나 로직상 Cookie가 맞음)
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(name))
                    .findFirst();
        }
        return Optional.empty();
    }

    // [핵심 수정] ResponseCookie를 사용하여 SameSite=None 설정 적용
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true)
                .secure(true)       // 🚨 중요: SameSite=None은 반드시 Secure=true여야 함 (HTTPS 필수)
                .sameSite("None")   // 👈 Apple 로그인 필수 설정
                .maxAge(maxAge)
                .domain("bangtal-boys.com")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    // [수정] 삭제 시에도 ResponseCookie를 사용하여 깔끔하게 삭제
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    ResponseCookie deleteCookie = ResponseCookie.from(name, "")
                            .path("/")
                            .maxAge(0)
                            .httpOnly(true)
                            .secure(true)
                            .sameSite("None")
                            .domain("bangtal-boys.com")
                            .build();
                    response.addHeader("Set-Cookie", deleteCookie.toString());
                }
            }
        }
    }

    // Byte 배열을 Base64 문자열로 변환 (직렬화 시)
    public static String serialize(Object object) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bo);
            os.writeObject(object);
            os.close();
            return Base64.getUrlEncoder().encodeToString(bo.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }

    // Base64 문자열을 객체로 역직렬화 (역직렬화 시)
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        byte[] bytes = Base64.getUrlDecoder().decode(cookie.getValue());
        try {
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bytes));
            @SuppressWarnings("unchecked")
            T object = (T) is.readObject();
            is.close();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException("Failed to deserialize cookie value: " + cookie.getValue(), e);
        }
    }
}