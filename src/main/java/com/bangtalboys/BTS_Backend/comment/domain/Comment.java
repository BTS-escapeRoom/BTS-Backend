package com.bangtalboys.BTS_Backend.comment.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long memberId;

    @Column(name = "board_id")
    private long boardId;

    @Column()
    private String comment;

    @Builder
    private Comment(long userId, long boardId, String comment) {
        this.memberId = userId;
        this.boardId = boardId;
        this.comment = comment;
    }
}
