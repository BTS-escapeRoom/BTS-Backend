package com.bangtalboys.BTS_Backend.theme.repository;

import com.bangtalboys.BTS_Backend.city.domain.QCity;
import com.bangtalboys.BTS_Backend.city.domain.QDistrict;
import com.bangtalboys.BTS_Backend.store.domain.QStore;
import com.bangtalboys.BTS_Backend.theme.domain.QTheme;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeListRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@AllArgsConstructor
public class ThemeCustomRepositoryImpl implements ThemeCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Theme> findThemes(ThemeListRequest req) {
        QTheme t = QTheme.theme;
        QStore s = QStore.store;
        QDistrict d = QDistrict.district;
        QCity c = QCity.city;

        BooleanBuilder where = buildWhere(req);

        JPQLQuery<Theme> query = jpaQueryFactory
                .selectFrom(t)
                .leftJoin(t.store, s).fetchJoin()
                .leftJoin(s.district, d).fetchJoin()
                .leftJoin(d.city, c).fetchJoin()
                .where(where)
                .distinct();

        // 정렬 처리
        if ("recent".equals(req.getSort())) {
            query.orderBy(t.registrationDate.desc());
        } else if ("popular".equals(req.getSort())) {
            // 서브쿼리로 좋아요 수를 계산해 정렬 (중복/그룹바이 이슈 회피)
            NumberExpression<Long> likeCount = Expressions.numberTemplate(
                    Long.class,
                    "(select count(1) from theme_like tl where tl.theme_id = {0})",
                    t.id
            );
            query.orderBy(likeCount.desc(), t.id.desc());
        } else if ("distance".equals(req.getSort()) && req.getLatitude() != null && req.getLongitude() != null) {
            // 좌표가 없는 가게는 제외
            where.and(s.latitude.isNotNull()).and(s.longitude.isNotNull());

            NumberExpression<Double> distance = Expressions.numberTemplate(
                    Double.class,
                    "ST_Distance_Sphere(POINT({0}, {1}), POINT({2}, {3}))",
                    req.getLongitude(), req.getLatitude(),
                    s.longitude, s.latitude
            );
            query.orderBy(distance.asc(), t.id.desc());
        }

        query.limit(20).offset((req.getPage()-1)* 20L);

        return query.fetch();
    }

    @Override
    public long countThemes(ThemeListRequest req) {
        QTheme t = QTheme.theme;
        QStore s = QStore.store;
        QDistrict d = QDistrict.district;
        QCity c = QCity.city;

        BooleanBuilder where = buildWhere(req);

        Long result = jpaQueryFactory
                .select(t.count())
                .from(t)
                .join(t.store, s)
                .join(s.district, d)
                .join(d.city, c)
                .where(where)
                .fetchOne();

        return result != null? result : 0L;
    }

    private BooleanBuilder buildWhere(ThemeListRequest req) {
        QTheme t = QTheme.theme;
        QStore s = QStore.store;
        QDistrict d = QDistrict.district;
        QCity c = QCity.city;

        BooleanBuilder where = new BooleanBuilder();

        if (req.getKeyword() != null && !req.getKeyword().isEmpty()) {
            where.and(t.title.contains(req.getKeyword()).or(s.name.contains(req.getKeyword())));
        }

        if (req.getPeoples() != null) {
            where.and(t.minimumPeople.loe(req.getPeoples()).and(t.maximumPeople.goe(req.getPeoples())));
        }

        if (req.getMinDiff() != null) {
            where.and(t.difficulty.goe(req.getMinDiff()));
        }

        if (req.getMaxDiff() != null) {
            where.and(t.difficulty.loe(req.getMaxDiff()));
        }

        if (req.getGenreIdList() != null && !req.getGenreIdList().isEmpty()) {
            where.and(t.genreType.id.in(req.getGenreIdList()));
        }

        if (req.getDistrictIdList() != null && !req.getDistrictIdList().isEmpty()) {
            where.and(s.district.id.in(req.getDistrictIdList()));
        }

        if (req.getCityIdList() != null && !req.getCityIdList().isEmpty()) {
            where.and(c.id.in(req.getCityIdList()));
        }

        return where;
    }
}
