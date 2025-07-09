package com.bantalboys.BTS_Backend.board.service;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.domain.BoardLike;
import com.bangtalboys.BTS_Backend.board.domain.BoardReport;
import com.bangtalboys.BTS_Backend.board.dto.request.BoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.request.UpdateBoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardResponse;
import com.bangtalboys.BTS_Backend.board.repository.BoardLikeRepository;
import com.bangtalboys.BTS_Backend.board.repository.BoardReportRepository;
import com.bangtalboys.BTS_Backend.board.repository.BoardRepository;
import com.bangtalboys.BTS_Backend.board.service.BoardService;
import com.bangtalboys.BTS_Backend.config.error.exception.ForbiddenException;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.repository.ThemeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ThemeRepository themeRepository;
    @Mock
    private BoardLikeRepository boardLikeRepository;
    @Mock
    private BoardReportRepository boardReportRepository;

    @Test
    void createBoard_success() {
        // given
        Long memberId = 1L;
        Member member = new Member();
        Theme theme = new Theme();

        BoardRequest request = new BoardRequest(
                10L,                        // themeId
                "normal",                  // type
                "제목",                    // title
                "본문 내용입니다",         // description
                new Date(),                // recruit_deadline
                new Date(),                // escape_date
                4L,                        // recruit_people
                "https://open.kakao.com/oo", // contact_url
                "카카오톡"                  // contact_method
        );

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(themeRepository.findById(request.getThemeId())).thenReturn(Optional.of(theme));
        ArgumentCaptor<Board> captor = ArgumentCaptor.forClass(Board.class);

        // when
        BoardResponse response = boardService.createBoard(request, memberId);

        // then
        verify(boardRepository).save(captor.capture());
        Board savedBoard = captor.getValue();
        assertNotNull(response);
        assertEquals("in-active", response.getReportStatus());
        assertEquals("제목", savedBoard.getTitle());
        assertEquals(4L, savedBoard.getRecruit_people());
    }

    @Test
    void updateBoard_success() {
        // given
        Long boardId = 1L;
        Long memberId = 2L;

        Member member = new Member();
        member.setId(memberId);

        Board board = new Board();
        board.setMember(member);  // 작성자 설정
        board.setTitle("기존 제목");
        board.setDescription("기존 본문");

        UpdateBoardRequest updateRequest = new UpdateBoardRequest("수정된 제목", "수정된 본문");

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(boardReportRepository.findByMemberAndBoard(any(), any())).thenReturn(Optional.empty());

        // when
        BoardResponse response = boardService.updateBoard(boardId, memberId, updateRequest);

        // then
        assertEquals("수정된 제목", board.getTitle());
        assertEquals("수정된 본문", board.getDescription());
        assertEquals("in-active", response.getReportStatus());
    }

    @Test
    void updateBoard_forbidden() {
        // given
        Long boardId = 1L;
        Long memberId = 2L;
        Member owner = new Member();
        owner.setId(99L);
        Board board = new Board();
        board.setMember(owner);

        UpdateBoardRequest request = new UpdateBoardRequest();

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // then
        assertThrows(ForbiddenException.class,
                () -> boardService.updateBoard(boardId, memberId, request));
    }

    @Test
    void createBoardLike_toggle() {
        // given
        Long memberId = 1L;
        Long boardId = 1L;
        Member member = new Member();
        Board board = new Board();
        BoardLike like = new BoardLike(member, board);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(boardLikeRepository.findByMemberAndBoard(member, board)).thenReturn(Optional.of(like));

        // when
        String result = boardService.createBoardLike(memberId, boardId);

        // then
        verify(boardLikeRepository).delete(like);
        assertEquals("게시글 찜 취소 완료", result);
    }
}
