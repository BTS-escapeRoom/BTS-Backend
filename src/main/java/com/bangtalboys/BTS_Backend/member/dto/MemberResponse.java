package com.bangtalboys.BTS_Backend.member.dto;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.utils.enums.Role;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.bangtalboys.BTS_Backend.utils.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberResponse {

    private Long id;
    private String profileImg;
    private String nickname;
    private String description;
    private SocialType socialType;
    private Role role;
    private Status status;
    private LocalDateTime createdAt;

    public MemberResponse(Member member) {
        id = member.getId();
        profileImg = member.getProfileImg();
        nickname = member.getNickname();
        description = member.getDescription();
        socialType = member.getSocialType();
        role = member.getRole();
        status = member.getStatus();
        createdAt = member.getCreatedAt();
    }
}
