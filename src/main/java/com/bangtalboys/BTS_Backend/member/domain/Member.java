package com.bangtalboys.BTS_Backend.member.domain;

import com.bangtalboys.BTS_Backend.utils.entity.BaseEntity;
import com.bangtalboys.BTS_Backend.utils.enums.Role;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.bangtalboys.BTS_Backend.utils.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profileImg;

    private String nickname;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @Builder
    public Member(Long id, String profileImg, String nickname, String description, SocialType socialType, String socialId, Role role, Status status) {
        this.id = id;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.description = description;
        this.socialType = socialType;
        this.socialId = socialId;
        this.role = role;
        this.status = status != null ? status : Status.ACTIVE;
    }
}
