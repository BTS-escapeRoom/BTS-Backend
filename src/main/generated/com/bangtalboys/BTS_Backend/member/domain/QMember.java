package com.bangtalboys.BTS_Backend.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -514363019L;

    public static final QMember member = new QMember("member1");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath profileImg = createString("profileImg");

    public final EnumPath<com.bangtalboys.BTS_Backend.utils.enums.Role> role = createEnum("role", com.bangtalboys.BTS_Backend.utils.enums.Role.class);

    public final EnumPath<com.bangtalboys.BTS_Backend.utils.enums.SocialType> socialType = createEnum("socialType", com.bangtalboys.BTS_Backend.utils.enums.SocialType.class);

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

