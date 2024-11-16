package com.bangtalboys.BTS_Backend.member.dto;

import lombok.Data;

@Data
public class MemberRequest {
    private String profileImg;
    private String nickname;
    private String description;
}

