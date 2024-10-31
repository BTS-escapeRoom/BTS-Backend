package com.bangtalboys.BTS_Backend.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "board")
public class Board {
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "theme_id")
    private Long themeId;

    @Column()
    private String type;

    @Column()
    private String title;

    private String description;

    private Long hit;


    @Builder
    public Board(Long id, Long userId, Long themeId, String type, String title, String description, Long hit) {
        this.id = id;
        this.userId = userId;
        this.themeId = themeId;
        this.type = type;
        this.title = title;
        this.description = description;
        this.hit = hit;
    }
}