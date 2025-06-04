package com.bangtalboys.BTS_Backend.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.Role;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import lombok.*;

@Getter
@Setter
@Builder
public class UserDto {

    private Long id;
    private String profileImg;
    private String nickname;
    private String socialType;
    private String socialId;
    private Role role;
}
