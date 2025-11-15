package com.bangtalboys.BTS_Backend.oauth.service;

import com.bangtalboys.BTS_Backend.oauth.provider.SocialLoginProvider;
import com.bangtalboys.BTS_Backend.oauth.dto.*;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.utils.enums.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppAuthService {

    private final OAuth2MemberService oAuth2MemberService;
    
    private final JwtUtil jwtUtil; 

    private final Map<String, SocialLoginProvider> providerMap;

    @Transactional
    public AuthResponse socialLogin(String provider, AppSocialLoginRequest req) {
        
        SocialLoginProvider socialLoginProvider = providerMap.get(provider.toLowerCase());
        if (socialLoginProvider == null) {
            throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다.");
        }

        OAuth2Response oAuth2Response = socialLoginProvider.getUserProfile(req);

        UserDto userDto = oAuth2MemberService.processOAuth2User(oAuth2Response);

        String accessToken = jwtUtil.createJwt(Token.AccessToken.getType(), userDto.getId(), userDto.getSocialType(), userDto.getSocialId(), userDto.getRole(), Token.AccessToken.getTtl()); 
        String refreshToken = jwtUtil.createJwt(Token.RefreshToken.getType(), userDto.getId(), userDto.getSocialType(), userDto.getSocialId(), userDto.getRole(), Token.RefreshToken.getTtl());

        // [신규] 5. JSON으로 응답
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberId(userDto.getId())
                .isNewUser(userDto.getIsNewUser())
                .role(userDto.getRole())
                .build();
    }
}
