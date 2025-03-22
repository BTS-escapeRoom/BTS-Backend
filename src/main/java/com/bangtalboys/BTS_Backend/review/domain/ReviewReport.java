package com.bangtalboys.BTS_Backend.review.domain;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "review_report")
public class ReviewReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String description;

    @Builder
    public ReviewReport(Review review, Member member, String description) {
        this.review = review;
        this.member = member;
        this.description = description;
    }
}