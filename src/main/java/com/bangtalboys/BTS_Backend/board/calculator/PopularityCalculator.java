package com.bangtalboys.BTS_Backend.board.calculator;

import com.bangtalboys.BTS_Backend.board.domain.Board;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class PopularityCalculator {

    private static final double POPULAR_THRESHOLD = 15.0;

    public static boolean isPopular(Board board) {
        int like = board.getLikes().size();
        int comment = board.getComments().size();
        long view = board.getHit();

        long days = getDaysSince(board.getCreated_at()); // ✅ 이제 Date

        double score = (like + comment * 1.5 + view * 0.1) / (days + 1);

        return score >= POPULAR_THRESHOLD;
    }

    // ✅ String → Date 기준으로 변경
    private static long getDaysSince(Date createdAt) {
        if (createdAt == null) {
            return 0L;
        }

        LocalDateTime created = Instant.ofEpochMilli(createdAt.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime now = LocalDateTime.now();

        return ChronoUnit.DAYS.between(created, now);
    }
}