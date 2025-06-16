package com.bangtalboys.BTS_Backend.member.dto;

import lombok.Data;

@Data
public class MemberUpdateRequest {
    private String profileImg;
    private String nickname;
    private String description;
}

