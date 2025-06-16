package com.bangtalboys.BTS_Backend.member.dto;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberCreateRequest {
    private String profileImg;

    @NotNull
    private String nickname;

    private String description;

    @NotNull
    private SocialType socialType;

    @NotNull
    private String socialId;
}
