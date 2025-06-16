package com.bangtalboys.BTS_Backend.member.dto;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import lombok.Data;

@Data
public class MemberCheckSignupRequest {
    private SocialType socialType;
    private String socialId;
}
