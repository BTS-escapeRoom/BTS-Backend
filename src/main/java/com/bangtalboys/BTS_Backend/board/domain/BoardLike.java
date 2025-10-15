package com.bangtalboys.BTS_Backend.board.domain;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "board_like")
public class BoardLike {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public BoardLike(Member member, Board board) {
        this.member = member;
        this.board = board;
    }
}
