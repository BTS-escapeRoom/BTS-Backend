package com.bangtalboys.BTS_Backend.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewReport is a Querydsl query type for ReviewReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewReport extends EntityPathBase<ReviewReport> {

    private static final long serialVersionUID = -1573318907L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewReport reviewReport = new QReviewReport("reviewReport");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.bangtalboys.BTS_Backend.member.domain.QMember member;

    public final QReview review;

    public QReviewReport(String variable) {
        this(ReviewReport.class, forVariable(variable), INITS);
    }

    public QReviewReport(Path<? extends ReviewReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewReport(PathMetadata metadata, PathInits inits) {
        this(ReviewReport.class, metadata, inits);
    }

    public QReviewReport(Class<? extends ReviewReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.bangtalboys.BTS_Backend.member.domain.QMember(forProperty("member")) : null;
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
    }

}

