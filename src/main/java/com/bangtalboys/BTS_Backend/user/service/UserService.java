package com.bangtalboys.BTS_Backend.user.service;

import com.bangtalboys.BTS_Backend.user.domain.User;
import com.bangtalboys.BTS_Backend.user.oauth.dto.AuthToken;
import com.bangtalboys.BTS_Backend.user.oauth.dto.AuthTokenGenerator;
import com.bangtalboys.BTS_Backend.user.oauth.dto.LoginRequest;
import com.bangtalboys.BTS_Backend.user.oauth.dto.UserInfoResponse;
import com.bangtalboys.BTS_Backend.user.oauth.service.OAuthService;
import com.bangtalboys.BTS_Backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthTokenGenerator authTokenGenerator;
    private final OAuthService oAuthService;

    public AuthToken login(LoginRequest loginRequest) {
        UserInfoResponse userInfoResponse = oAuthService.request(loginRequest);
        Long memberId = findOrCreateUser(userInfoResponse);
        return authTokenGenerator.generate(memberId);
    }

    private Long findOrCreateUser(UserInfoResponse userInfoResponse) {
        return userRepository.findBySocialTypeAndSocialId(userInfoResponse.getSocialType(),userInfoResponse.getSocialId())
                .map(User::getId)
                .orElseGet(() -> createUser(userInfoResponse));
    }

    private Long createUser(UserInfoResponse userInfoResponse) {
        User user = User.builder()
                .profileImg(userInfoResponse.getProfileImg())
                .nickname(userInfoResponse.getNickname())
                .socialType(userInfoResponse.getSocialType())
                .socialId(userInfoResponse.getSocialId())
                .build();

        return userRepository.save(user).getId();
    }
}
