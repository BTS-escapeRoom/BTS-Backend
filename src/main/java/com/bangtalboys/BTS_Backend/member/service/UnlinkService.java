package com.bangtalboys.BTS_Backend.member.service;

import com.bangtalboys.BTS_Backend.member.client.KakaoUnlinkClient;
import com.bangtalboys.BTS_Backend.member.client.NaverUnlinkClient;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnlinkService {
    private final KakaoUnlinkClient kakaoUnlinkClient;
    private final NaverUnlinkClient naverUnlinkClient;

    @Value("${kakao.admin-key}") // application.yml 등에 설정
    private String adminKey;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    public void unlinkKakao(String socialId) {
        String authHeader = "KakaoAK " + adminKey;
        kakaoUnlinkClient.unlinkUser(authHeader, "user_id", socialId);
    }

    public void unlinkNaver(String accessToken) {
        naverUnlinkClient.unlinkUser(
                "delete",
                clientId,
                clientSecret,
                accessToken,
                SocialType.NAVER.toString()
        );
    }
}