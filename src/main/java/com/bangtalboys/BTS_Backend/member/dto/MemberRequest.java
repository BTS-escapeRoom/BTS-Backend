package com.bangtalboys.BTS_Backend.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequest {
    private Long id;
    private String nickname;

    public MemberRequest(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}

