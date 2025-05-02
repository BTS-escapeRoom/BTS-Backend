package com.bangtalboys.BTS_Backend.theme.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTheme is a Querydsl query type for Theme
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTheme extends EntityPathBase<Theme> {

    private static final long serialVersionUID = -989836311L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTheme theme = new QTheme("theme");

    public final StringPath description = createString("description");

    public final NumberPath<Long> difficulty = createNumber("difficulty", Long.class);

    public final StringPath genre = createString("genre");

    public final com.bangtalboys.BTS_Backend.genre.domain.QGenre genreType;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> maximumPeople = createNumber("maximumPeople", Integer.class);

    public final NumberPath<Integer> minimumPeople = createNumber("minimumPeople", Integer.class);

    public final StringPath notes = createString("notes");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> recommendPeople = createNumber("recommendPeople", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> registrationDate = createDateTime("registrationDate", java.time.LocalDateTime.class);

    public final StringPath reservationUrl = createString("reservationUrl");

    public final NumberPath<Integer> scareScore = createNumber("scareScore", Integer.class);

    public final StringPath status = createString("status");

    public final com.bangtalboys.BTS_Backend.store.domain.QStore store;

    public final ListPath<ThemeLike, QThemeLike> themeLikeList = this.<ThemeLike, QThemeLike>createList("themeLikeList", ThemeLike.class, QThemeLike.class, PathInits.DIRECT2);

    public final ListPath<ThemeTime, QThemeTime> themeTimeList = this.<ThemeTime, QThemeTime>createList("themeTimeList", ThemeTime.class, QThemeTime.class, PathInits.DIRECT2);

    public final StringPath thumbnail = createString("thumbnail");

    public final NumberPath<Integer> time = createNumber("time", Integer.class);

    public final StringPath title = createString("title");

    public QTheme(String variable) {
        this(Theme.class, forVariable(variable), INITS);
    }

    public QTheme(Path<? extends Theme> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTheme(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTheme(PathMetadata metadata, PathInits inits) {
        this(Theme.class, metadata, inits);
    }

    public QTheme(Class<? extends Theme> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.genreType = inits.isInitialized("genreType") ? new com.bangtalboys.BTS_Backend.genre.domain.QGenre(forProperty("genreType")) : null;
        this.store = inits.isInitialized("store") ? new com.bangtalboys.BTS_Backend.store.domain.QStore(forProperty("store"), inits.get("store")) : null;
    }

}

