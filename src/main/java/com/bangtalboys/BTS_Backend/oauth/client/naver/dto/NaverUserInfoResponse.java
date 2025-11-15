package com.bangtalboys.BTS_Backend.oauth.client.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@NoArgsConstructor
public class NaverUserInfoResponse {
    private String resultcode;
    private String message;
    private Response response;
    
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private String id;
        private String email;
        private String nickname;
        private String name;
        private String profileImage;
        private String mobile;
        private String mobileE164;
        private String birthday;
        private String birthyear;
        private String gender;
    }
}

