package com.bangtalboys.BTS_Backend.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 1327244913L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.bangtalboys.BTS_Backend.utils.entity.QBaseEntity _super = new com.bangtalboys.BTS_Backend.utils.entity.QBaseEntity(this);

    public final NumberPath<Integer> activityScore = createNumber("activityScore", Integer.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Float> difficulty = createNumber("difficulty", Float.class);

    public final NumberPath<Integer> hints = createNumber("hints", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isSuccess = createBoolean("isSuccess");

    public final com.bangtalboys.BTS_Backend.member.domain.QMember member;

    public final NumberPath<Integer> people = createNumber("people", Integer.class);

    public final NumberPath<Integer> scareScore = createNumber("scareScore", Integer.class);

    public final com.bangtalboys.BTS_Backend.theme.domain.QTheme theme;

    public final NumberPath<Integer> time = createNumber("time", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final DateTimePath<java.time.LocalDateTime> visitDate = createDateTime("visitDate", java.time.LocalDateTime.class);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.bangtalboys.BTS_Backend.member.domain.QMember(forProperty("member")) : null;
        this.theme = inits.isInitialized("theme") ? new com.bangtalboys.BTS_Backend.theme.domain.QTheme(forProperty("theme"), inits.get("theme")) : null;
    }

}

