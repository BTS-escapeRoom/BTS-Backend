package com.bangtalboys.BTS_Backend.board.service;


import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.domain.BoardLike;
import com.bangtalboys.BTS_Backend.board.domain.BoardReport;
import com.bangtalboys.BTS_Backend.board.dto.request.BoardListRequest;
import com.bangtalboys.BTS_Backend.board.dto.request.BoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.request.UpdateBoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardListPageResponse;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardResponse;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardListResponse;
import com.bangtalboys.BTS_Backend.board.repository.BoardLikeRepository;
import com.bangtalboys.BTS_Backend.board.repository.BoardReportRepository;
import com.bangtalboys.BTS_Backend.board.repository.BoardRepository;
import com.bangtalboys.BTS_Backend.config.error.exception.ForbiddenException;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.repository.ThemeRepository;
import com.bangtalboys.BTS_Backend.utils.enums.BoardType;
import com.bangtalboys.BTS_Backend.utils.enums.SortType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardReportRepository boardReportRepository;

    @Transactional
    public BoardResponse createBoard(BoardRequest boardRequest, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        Theme theme = themeRepository.findById(boardRequest.getThemeId()).orElseThrow(NotFoundException::new);
        Board board = new Board(boardRequest, member, theme);
        boardRepository.save(board);
        return new BoardResponse(board, "in-active");
    }

    @Transactional
    public BoardResponse getOneBoard(Long boardId, Long memberId) {
        Optional<Board> board = boardRepository.findById(boardId);
        if (!board.isPresent()) {
            throw new NotFoundException();
        }
        board.get().setHit(board.get().getHit() + 1);
        if (memberId != null) {
            Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
            Optional<BoardReport> boardReport = boardReportRepository.findByMemberAndBoard(member, board.get());
            if (boardReport.isPresent()) {
                return new BoardResponse(board.get(), boardReport.get().getStatus());
            }
        }

        return new BoardResponse(board.get(), "in-active");
    }
    public BoardListPageResponse getAllBoards(BoardListRequest boardListRequest) {
        List<Board> boards = boardRepository.findBoards(boardListRequest);
        long totalCount = boardRepository.countBoards(boardListRequest);
        long totalPage = totalCount % 20 == 0 ? totalCount / 20 : totalCount / 20 + 1;
        long nextPage = boardListRequest.getPage() + 1L;

        if (boardListRequest.getPage() == totalPage) {
            nextPage = -1L;
        }

        List<BoardListResponse> boardListResponses = boards.stream().map(BoardListResponse::new).toList();
        return new BoardListPageResponse(boardListResponses, nextPage, totalPage);
    }


    public BoardResponse updateBoard(Long boardId, Long memberId, UpdateBoardRequest updateBoardRequest) {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
        if (!board.getMember().getId().equals(memberId)) {
            throw new ForbiddenException();
        }
        board.setTitle(updateBoardRequest.getTitle());
        board.setDescription(updateBoardRequest.getDescription());
        return this.getOneBoard(boardId, memberId);
    }

    public String deleteBoard(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
        if (!board.getMember().getId().equals(userId)) {
            throw new ForbiddenException();
        }

        boardRepository.delete(board);
        return "Board deleted";
    }

    public String createBoardLike(Long memberId, Long boardId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);

        // 기존에 찜한 상태인지 확인
        Optional<BoardLike> existLike = boardLikeRepository.findByMemberAndBoard(member, board);

        if (existLike.isPresent()) {
            boardLikeRepository.delete(existLike.get());
            return "게시글 찜 취소 완료";
        } else {
            BoardLike boardLike = new BoardLike(member, board);
            boardLikeRepository.save(boardLike);
            return "게시글 찜 설정 완료";
        }
    }

    public String createBoardReport(Long memberId, Long boardId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);

        Optional<BoardReport> existReport = boardReportRepository.findByMemberAndBoard(member, board);

        if (existReport.isPresent()) {
            boardReportRepository.delete(existReport.get());
            return "게시글 신고 취소 완료";
        }
        BoardReport boardReport = new BoardReport(member, board, "active");
        boardReportRepository.save(boardReport);
        return "게시글 신고 완료";
    }

    public List<BoardListResponse> getLikeBoard(Long memberId) {
        List<Board> boards = boardRepository.findLikedBoardByMemberId(memberId);
        return boards.stream().map(BoardListResponse::new).collect(Collectors.toList());
    }

    public List<BoardListResponse> getMyBoardList(Long memberId) {
        List<Board> boards = boardRepository.findAllByMemberId(memberId);
        return boards.stream().map(BoardListResponse::new).collect(Collectors.toList());
    }

}