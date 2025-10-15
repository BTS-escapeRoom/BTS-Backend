package com.bangtalboys.BTS_Backend.oauth.dto;

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
    private String role;
    private Boolean isNewUser;
}
