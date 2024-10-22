package com.bangtalboys.BTS_Backend.member.domain;

import com.bangtalboys.BTS_Backend.utils.enums.Role;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profileImg;

    private String nickname;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    private String username;

    @Enumerated(value = EnumType.STRING)
    private Role role;


    @Builder
    public Member(Long id, String profileImg, String nickname, String description, SocialType socialType, String username, Role role) {
        this.id = id;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.description = description;
        this.socialType = socialType;
        this.username = username;
        this.role = role;
    }
}
