package com.bangtalboys.BTS_Backend.store.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStore is a Querydsl query type for Store
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStore extends EntityPathBase<Store> {

    private static final long serialVersionUID = 1498944233L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStore store = new QStore("store");

    public final StringPath description = createString("description");

    public final com.bangtalboys.BTS_Backend.city.domain.QDistrict district;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final StringPath location = createString("location");

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath name = createString("name");

    public final DateTimePath<java.time.LocalDateTime> registrationDate = createDateTime("registrationDate", java.time.LocalDateTime.class);

    public final ListPath<com.bangtalboys.BTS_Backend.theme.domain.Theme, com.bangtalboys.BTS_Backend.theme.domain.QTheme> themeList = this.<com.bangtalboys.BTS_Backend.theme.domain.Theme, com.bangtalboys.BTS_Backend.theme.domain.QTheme>createList("themeList", com.bangtalboys.BTS_Backend.theme.domain.Theme.class, com.bangtalboys.BTS_Backend.theme.domain.QTheme.class, PathInits.DIRECT2);

    public final StringPath thumbnail = createString("thumbnail");

    public QStore(String variable) {
        this(Store.class, forVariable(variable), INITS);
    }

    public QStore(Path<? extends Store> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStore(PathMetadata metadata, PathInits inits) {
        this(Store.class, metadata, inits);
    }

    public QStore(Class<? extends Store> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.district = inits.isInitialized("district") ? new com.bangtalboys.BTS_Backend.city.domain.QDistrict(forProperty("district"), inits.get("district")) : null;
    }

}

