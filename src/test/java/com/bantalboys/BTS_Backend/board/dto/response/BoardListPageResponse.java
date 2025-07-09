package com.bantalboys.BTS_Backend.board.dto.response;

import com.bangtalboys.BTS_Backend.board.dto.response.BoardListResponse;
import lombok.Data;

import java.util.List;

@Data
public class BoardListPageResponse {
    private List<BoardListResponse> boards;
    private long nextPage;
    private long totalPage;

    public BoardListPageResponse(List<BoardListResponse> boards, long nextPage, long totalPage) {
        this.boards = boards;
        this.nextPage = nextPage;
        this.totalPage = totalPage;
    }
}
