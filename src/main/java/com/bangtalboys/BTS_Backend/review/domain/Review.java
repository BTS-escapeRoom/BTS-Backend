package com.bangtalboys.BTS_Backend.review.domain;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.utils.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Integer people;

    private Integer time;

    private Integer scareScore;

    private Integer activityScore;

    private Integer hardScore;

    private LocalDateTime visitDate;

    private boolean isSuccess;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Review(String content, Integer people, Integer time, Integer scareScore, Integer activityScore, Integer hardScore, LocalDateTime visitDate, boolean isSuccess, Theme theme, Member member) {
        this.content = content;
        this.people = people;
        this.time = time;
        this.scareScore = scareScore;
        this.activityScore = activityScore;
        this.hardScore = hardScore;
        this.visitDate = visitDate;
        this.isSuccess = isSuccess;
        this.theme = theme;
        this.member = member;
    } {

    }
}
