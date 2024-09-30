package com.bangtalboys.BTS_Backend.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequest {
    private Long id;
    private String nickname;

    public UserRequest(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}

