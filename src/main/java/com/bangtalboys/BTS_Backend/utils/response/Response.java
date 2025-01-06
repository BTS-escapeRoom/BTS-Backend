package com.bangtalboys.BTS_Backend.utils.response;

import lombok.Getter;
import lombok.Setter;
@Getter
public class Response<T> {
    private String code;
    private String message;
    // Getter 메서드
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

}
