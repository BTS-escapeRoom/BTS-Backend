package com.bangtalboys.BTS_Backend.comment.domain;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name="comment_report")
public class CommentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column()
    private String status;

    @Builder
    public CommentReport(Member member, Comment comment, String status) {
        this.member = member;
        this.comment = comment;
        this.status = status;
    }
}
