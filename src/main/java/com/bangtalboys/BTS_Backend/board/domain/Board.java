package com.bangtalboys.BTS_Backend.board.domain;

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

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "theme_id")
    private Long themeId;

    @Column()
    private String type;

    @Column()
    private String title;

    private String description;

    private Long hit;


    @Builder
    public Board(Long id, Long memberId, Long themeId, String type, String title, String description, Long hit) {
        this.id = id;
        this.memberId = memberId;
        this.themeId = themeId;
        this.type = type;
        this.title = title;
        this.description = description;
        this.hit = hit;
    }
}