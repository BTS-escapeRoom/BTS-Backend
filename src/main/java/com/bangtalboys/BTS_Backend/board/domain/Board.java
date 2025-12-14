package com.bangtalboys.BTS_Backend.board.domain;

import com.bangtalboys.BTS_Backend.board.dto.request.BoardRequest;
import com.bangtalboys.BTS_Backend.comment.domain.Comment;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.utils.enums.BoardType;
import com.bangtalboys.BTS_Backend.utils.enums.ContactMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Column()
    @Enumerated(EnumType.STRING)
    private BoardType type;

    @Column()
    private String title;

    private String description;

    private Long hit = 0L;

    private Date recruit_deadline;

    private Date escape_date;

    private Long recruit_people;

    private String contact_url;

    @Column()
    @Enumerated(EnumType.STRING)
    private ContactMethod contact_method;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BoardLike> likes = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @CreationTimestamp
    @Column()
    private Date created_at;

    @UpdateTimestamp
    @Column()
    private Date updated_at;

    @Builder
    public Board( BoardRequest boardRequest, Member member, Theme theme) {
        this.member = member;
        this.theme = theme;
        this.type = boardRequest.getType();
        this.title = boardRequest.getTitle();
        this.description = boardRequest.getDescription();
        this.recruit_deadline = boardRequest.getRecruit_deadline();
        this.escape_date = boardRequest.getEscape_date();
        this.recruit_people = boardRequest.getRecruit_people();
        this.contact_url = boardRequest.getContact_url();
        this.contact_method = boardRequest.getContact_method();
        this.hit = 0L;
    }
}