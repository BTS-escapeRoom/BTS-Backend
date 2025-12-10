package com.bangtalboys.BTS_Backend.board.repository;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.domain.QBoard;
import com.bangtalboys.BTS_Backend.board.domain.QBoardLike;
import com.bangtalboys.BTS_Backend.board.dto.request.BoardListRequest;
import com.bangtalboys.BTS_Backend.store.domain.QStore;
import com.bangtalboys.BTS_Backend.theme.domain.QTheme;
import com.bangtalboys.BTS_Backend.utils.enums.SortType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@AllArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Board> findBoards(BoardListRequest boardListRequest) {
        QBoard b = QBoard.board;
        QTheme t = QTheme.theme;
        QStore s = QStore.store;
        QBoardLike bl = QBoardLike.boardLike;

        BooleanBuilder where = buildWhere(boardListRequest);

        JPQLQuery<Board> query = jpaQueryFactory
                .selectFrom(b)
                .leftJoin(b.theme, t).fetchJoin()
                .leftJoin(t.store, s).fetchJoin()
                .leftJoin(b.likes, bl)
                .where(where)
                .distinct();

        if (SortType.viewed.equals(boardListRequest.getSortType())) {
            query.orderBy(b.hit.desc());
        } else if (SortType.popular.equals(boardListRequest.getSortType())) {
            query.groupBy(b).orderBy(bl.count().desc());
        }else if (SortType.old.equals(boardListRequest.getSortType())) {
            query.orderBy(b.created_at.asc());
        }
        query.orderBy(b.created_at.desc());

        query.limit(20).offset((boardListRequest.getPage()-1)* 20L);
        return query.fetch();
    }

    @Override
    public long countBoards(BoardListRequest boardListRequest) {
        QBoard b = QBoard.board;
        QTheme t = QTheme.theme;
        QStore s = QStore.store;

        BooleanBuilder where = buildWhere(boardListRequest);

        Long count = jpaQueryFactory
                .select(b.id.countDistinct())
                .from(b)
                .leftJoin(b.theme, t)
                .leftJoin(t.store, s)
                .where(where)
                .fetchOne();

        return count != null ? count : 0L;
    }

    private BooleanBuilder buildWhere(BoardListRequest boardListRequest) {
        QBoard b = QBoard.board;

        BooleanBuilder where = new BooleanBuilder();

        // ✅ 키워드 검색
        if (boardListRequest.getKeyword() != null && !boardListRequest.getKeyword().isEmpty()) {
            where.and(
                    b.title.contains(boardListRequest.getKeyword())
                            .or(b.description.contains(boardListRequest.getKeyword()))
                            .or(b.theme.title.contains(boardListRequest.getKeyword()))
                            .or(b.theme.store.name.contains(boardListRequest.getKeyword()))
            );
        }

        // ✅ 게시글 타입 필터
        if (boardListRequest.getBoardType() != null) {
            where.and(b.type.eq(boardListRequest.getBoardType()));
        }

        // ✅ 모집중인 글만 보기 (🔥 여기 추가 🔥)
        if (boardListRequest.isRecruiting()) {
            where.and(b.recruit_deadline.after(new Date()));
        }

        return where;
    }

}
