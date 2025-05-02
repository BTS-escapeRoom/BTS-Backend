package com.bangtalboys.BTS_Backend.theme.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QThemeTime is a Querydsl query type for ThemeTime
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QThemeTime extends EntityPathBase<ThemeTime> {

    private static final long serialVersionUID = 1927149142L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QThemeTime themeTime = new QThemeTime("themeTime");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QTheme theme;

    public final StringPath time = createString("time");

    public final StringPath timeType = createString("timeType");

    public QThemeTime(String variable) {
        this(ThemeTime.class, forVariable(variable), INITS);
    }

    public QThemeTime(Path<? extends ThemeTime> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QThemeTime(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QThemeTime(PathMetadata metadata, PathInits inits) {
        this(ThemeTime.class, metadata, inits);
    }

    public QThemeTime(Class<? extends ThemeTime> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.theme = inits.isInitialized("theme") ? new QTheme(forProperty("theme"), inits.get("theme")) : null;
    }

}

