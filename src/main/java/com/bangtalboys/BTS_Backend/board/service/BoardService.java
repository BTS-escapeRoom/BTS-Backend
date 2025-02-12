package com.bangtalboys.BTS_Backend.board.service;


import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.domain.BoardLike;
import com.bangtalboys.BTS_Backend.board.dto.request.BoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.request.UpdateBoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardResponse;
import com.bangtalboys.BTS_Backend.board.dto.response.ListBoardResponse;
import com.bangtalboys.BTS_Backend.board.repository.BoardLikeRepository;
import com.bangtalboys.BTS_Backend.board.repository.BoardRepository;
import com.bangtalboys.BTS_Backend.config.error.exception.ForbiddenException;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public BoardResponse createBoard(BoardRequest boardRequest, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        Theme theme = themeRepository.findById(boardRequest.getThemeId()).orElseThrow(NotFoundException::new);
        Board board = new Board(boardRequest, member, theme);
        boardRepository.save(board);
        return new BoardResponse(board, member, theme);
    }

    public BoardResponse getOneBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);

        board.setHit(board.getHit()+1);

        Member member = memberRepository.findById(board.getMember().getId()).orElseThrow(NotFoundException::new);
        Theme theme = themeRepository.findById(board.getTheme().getId()).orElseThrow(NotFoundException::new);
        return new BoardResponse(board, member, theme);
    }

    public List<ListBoardResponse> getAllBoards() {
        List<ListBoardResponse> boardResponseList = new ArrayList<>();
        List<Board> boardList = boardRepository.findAll();
        for (Board board : boardList) {
            ListBoardResponse listBoardResponse = new ListBoardResponse(board);
            boardResponseList.add(listBoardResponse);
        }
        return boardResponseList;
    }

    public BoardResponse updateBoard(Long boardId, Long memberId, UpdateBoardRequest updateBoardRequest) {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
        if (!board.getMember().getId().equals(memberId)) {
            throw new ForbiddenException();
        }
        board.setTitle(updateBoardRequest.getTitle());
        board.setDescription(updateBoardRequest.getDescription());
        return this.getOneBoard(boardId);
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

    public List<ListBoardResponse> getLikeBoard(Long memberId) {
        List<Board> boards = boardRepository.findBoardByMemberId(memberId);
        return boards.stream().map(ListBoardResponse::new).collect(Collectors.toList());
    }

    public List<ListBoardResponse> searchBoard(String keyword) {
        List<Board> boards = boardRepository.searchBoardByKeyword(keyword);
        return boards.stream().map(ListBoardResponse::new).collect(Collectors.toList());
    }
}