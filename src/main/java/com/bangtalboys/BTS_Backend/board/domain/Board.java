package com.bangtalboys.BTS_Backend.board.domain;

import com.bangtalboys.BTS_Backend.board.dto.request.BoardRequest;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Column()
    private String type;

    @Column()
    private String title;

    private String description;

    private Long hit;


    @Builder
    public Board( BoardRequest boardRequest, Member member, Theme theme) {
        this.member = member;
        this.theme = theme;
        this.type = boardRequest.getType();
        this.title = boardRequest.getTitle();
        this.description = boardRequest.getDescription();
        this.hit = 0L;
    }
}