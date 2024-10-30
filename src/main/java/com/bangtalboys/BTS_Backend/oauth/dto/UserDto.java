package com.bangtalboys.BTS_Backend.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.Role;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import lombok.*;

@Getter
@Setter
@Builder
public class UserDto {

    private String profileImg;
    private String nickname;
    private SocialType socialType;
    private String username;
    private Role role;
}
