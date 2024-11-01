package com.bangtalboys.BTS_Backend.utils.Response;

import org.springframework.http.HttpStatus;

public class Response<T> {
    private String code;
    private String message;
    private T data;


    public Response(T data, String message, String code) {
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public static <T> Response<T> ok(T data) {
        return new Response<>(data, null, "200");
    }

    public static <T> Response<T> error(String message, String code) {
        return new Response<>(null, message, code);
    }

    // Getter 메서드
    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
