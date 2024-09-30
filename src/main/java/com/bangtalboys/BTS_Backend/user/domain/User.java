package com.bangtalboys.BTS_Backend.user.domain;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    private Long id;

    private String profileImg;

    private String nickname;

    private String description;

    private SocialType socialType;

    private Long socialId;

    @Builder
    public User(Long id, String profileImg, String nickname, String description, SocialType socialType, Long socialId) {
        this.id = id;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.description = description;
        this.socialType = socialType;
        this.socialId = socialId;
    }
}
