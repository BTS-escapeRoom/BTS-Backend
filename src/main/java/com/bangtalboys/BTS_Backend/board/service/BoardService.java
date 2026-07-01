package com.bangtalboys.BTS_Backend.board.service;


import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.domain.BoardLike;
import com.bangtalboys.BTS_Backend.board.domain.BoardReport;
import com.bangtalboys.BTS_Backend.board.dto.request.*;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardListPageResponse;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardResponse;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardListResponse;
import com.bangtalboys.BTS_Backend.board.repository.BoardLikeRepository;
import com.bangtalboys.BTS_Backend.board.repository.BoardReportRepository;
import com.bangtalboys.BTS_Backend.board.repository.BoardRepository;
import com.bangtalboys.BTS_Backend.comment.repository.CommentRepository;
import com.bangtalboys.BTS_Backend.config.error.exception.ForbiddenException;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.repository.ThemeRepository;
import com.bangtalboys.BTS_Backend.utils.enums.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional  // open-in-view=false: 서비스 메서드 내 LAZY 연관(Board.theme 등) 접근/쓰기 반영 보장
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardReportRepository boardReportRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public BoardResponse createBoard(BoardRequest boardRequest, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        Theme theme = null;
        if (boardRequest.getThemeId() != null) {
            theme = themeRepository.findById(boardRequest.getThemeId()).orElseThrow(NotFoundException::new);
        }
        Board board = new Board(boardRequest, member, theme);
        boardRepository.save(board);
        return new BoardResponse(board,false, false, 0, 0);
    }

    @Transactional
    public BoardResponse getOneBoard(Long boardId, Long memberId) {

        Board board = boardRepository.findDetailById(boardId)
                .orElseThrow(NotFoundException::new);

        // 조회수 증가
        board.setHit(board.getHit() + 1);

        // ✅ 컬렉션 로딩 대신 count 쿼리로 개수만
        long likeCount = boardLikeRepository.countByBoardId(boardId);
        long commentCount = commentRepository.countByBoardId(boardId);

        // 기본값
        boolean isLike = false;
        boolean isReported = false;
        String status = Status.INACTIVE.name();

        if (memberId != null) {
            // ✅ member 조회 없이 memberId로만 처리 (쿼리/메모리 둘 다 절약)
            status = boardReportRepository.findStatusByMemberIdAndBoardId(memberId, boardId)
                    .orElse(Status.INACTIVE.name());
            if (status.equals(Status.ACTIVE.name())) {
                isReported = true;
            }

            isLike = boardLikeRepository.existsByMemberIdAndBoardId(memberId, boardId);
        }

        return new BoardResponse(board, isReported, isLike, likeCount, commentCount);
    }

    public BoardListPageResponse getAllBoards(BoardListRequest boardListRequest, Long loginMemberId) {
        List<Board> boards = boardRepository.findBoards(boardListRequest);
        long totalCount = boardRepository.countBoards(boardListRequest);
        long totalPage = totalCount % 20 == 0 ? totalCount / 20 : totalCount / 20 + 1;
        long nextPage = boardListRequest.getPage() + 1L;

        if (boardListRequest.getPage() == totalPage) {
            nextPage = -1L;
        }

        List<Long> boardIds = boards.stream()
                .map(Board::getId)
                .toList();

        Set<Long> reportedBoardIdSet = boardIds.isEmpty()
                ? Collections.emptySet()
                : new HashSet<>(boardReportRepository.findReportedBoardIds(loginMemberId, boardIds));

        List<BoardListResponse> boardListResponses = boards.stream()
                .map(board -> new BoardListResponse(board, reportedBoardIdSet.contains(board.getId())))
                .toList();

        return new BoardListPageResponse(boardListResponses, nextPage, totalPage);
    }


    public BoardResponse updateBoard(Long boardId, Long memberId, UpdateBoardRequest updateBoardRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(NotFoundException::new);

        if (!board.getMember().getId().equals(memberId)) {
            throw new ForbiddenException();
        }

        board.setTitle(updateBoardRequest.getTitle());
        board.setDescription(updateBoardRequest.getDescription());
        board.setRecruit_deadline(updateBoardRequest.getRecruit_deadline());
        board.setEscape_date(updateBoardRequest.getEscape_date());
        board.setRecruit_people(updateBoardRequest.getRecruit_people());
        board.setContact_url(updateBoardRequest.getContact_url());
        board.setContact_method(updateBoardRequest.getContact_method());

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

    public String createBoardReport(Long memberId, BoardReportRequest boardReportRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        Board board = boardRepository.findById(boardReportRequest.getBoardId()).orElseThrow(NotFoundException::new);

        Optional<BoardReport> existReport = boardReportRepository.findByMemberAndBoard(member, board);

        if (existReport.isPresent()) {
            boardReportRepository.delete(existReport.get());
            return "게시글 신고 취소 완료";
        }
        BoardReport boardReport = new BoardReport(member, board, Status.ACTIVE.name(), boardReportRequest.getDescription());
        boardReportRepository.save(boardReport);
        return "게시글 신고 완료";
    }

    public List<BoardListResponse> getLikeBoard(Long memberId) {
        List<Board> boards = boardRepository.findLikedBoardByMemberId(memberId);
        return toBoardListResponses(boards, memberId);
    }

    public List<BoardListResponse> getMyBoardList(Long memberId) {
        List<Board> boards = boardRepository.findAllByMemberId(memberId);
        return toBoardListResponses(boards, memberId);
    }

    private List<BoardListResponse> toBoardListResponses(List<Board> boards, Long memberId) {
        List<Long> boardIds = boards.stream()
                .map(Board::getId)
                .toList();

        Set<Long> reportedBoardIdSet = boardIds.isEmpty()
                ? Collections.emptySet()
                : new HashSet<>(boardReportRepository.findReportedBoardIds(memberId, boardIds));

        return boards.stream()
                .map(board -> new BoardListResponse(board, reportedBoardIdSet.contains(board.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public String closeRecruit(Long boardId, Long memberId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.")
                );

        if (!board.getMember().getId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자가 아닙니다.");
        }

        Date now = new Date();
        if (board.getRecruit_deadline().before(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 모집 마감된 글입니다.");
        }

        board.setRecruit_deadline(now);

        return "모집 마감처리되었습니다.";
    }

    @Transactional
    public String reopenRecruit(Long boardId, Long memberId, ReopenRecruitRequest request) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.")
                );

        if (!board.getMember().getId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자가 아닙니다.");
        }

        Date now = new Date();
        Date newRecruitDeadline = request.getRecruitDeadline();

        if (newRecruitDeadline == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "새로운 모집 마감일이 필요합니다.");
        }

        if (!newRecruitDeadline.after(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "모집 마감일은 현재 시간 이후여야 합니다.");
        }

        if (board.getRecruit_deadline() != null && board.getRecruit_deadline().after(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아직 모집 중인 글입니다.");
        }

        board.setRecruit_deadline(newRecruitDeadline);

        return "모집 마감이 취소되었습니다.";
    }
}