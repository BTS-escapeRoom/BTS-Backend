package com.bangtalboys.BTS_Backend.member.dto;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.utils.enums.Role;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import lombok.Data;

@Data
public class MemberResponse {

    private Long id;
    private String profileImg;
    private String nickname;
    private String description;
    private SocialType socialType;
    private Role role;

    public MemberResponse(Member member) {
        id = member.getId();
        profileImg = member.getProfileImg();
        nickname = member.getNickname();
        description = member.getDescription();
        socialType = member.getSocialType();
        role = member.getRole();
    }
}
