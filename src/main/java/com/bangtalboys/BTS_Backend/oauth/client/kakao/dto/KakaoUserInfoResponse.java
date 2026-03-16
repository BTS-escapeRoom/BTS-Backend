package com.bangtalboys.BTS_Backend.oauth.client.kakao.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoResponse {
    private Long id;
    private String connectedAt;
    private KakaoAccount kakaoAccount;
    
    /**
     * 카카오 계정 정보
     */
    @Getter
    @NoArgsConstructor
    public static class KakaoAccount {
        private Boolean profileNeedsAgreement;
        private Profile profile;
        private String email;
        
        /**
         * 프로필 정보
         */
        @Getter
        @NoArgsConstructor
        public static class Profile {
            private String nickname;
            private String thumbnailImageUrl;
            private String profileImageUrl;
        }
    }
}