package com.bantalboys.BTS_Backend.board.controller;

import com.bangtalboys.BTS_Backend.BtsBackendApplication;
import com.bangtalboys.BTS_Backend.board.dto.request.BoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.request.UpdateBoardRequest;
import com.bangtalboys.BTS_Backend.utils.enums.BoardType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(classes = BtsBackendApplication.class)
@AutoConfigureMockMvc
public class BoardControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 글 생성
    @Test
    void createBoard_E2E() throws Exception {
        BoardRequest request = new BoardRequest(
                1L, BoardType.NORMAL, "Test Title", "Test Description",
                new Date(), new Date(), 3L,
                "https://open.kakao.com/abc", "KakaoTalk"
        );

        mockMvc.perform(post("/v1/boards")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 인증 없이 글 쓰면 403
//    @Test
//    void createBoard_withoutAuth_shouldFail() throws Exception {
//        SecurityContextHolder.clearContext(); // 인증 제거
//
//        BoardRequest request = new BoardRequest(
//                1L, "normal", "Unauthorized", "Should fail",
//                new Date(), new Date(), 2L,
//                "https://url", "email"
//        );
//
//        mockMvc.perform(post("/v1/boards")
//                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0LXVzZXIiLCJyb2xlIjoiR1VFU1QifQ.dummysignature1234567890")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isUnauthorized()); // or isForbidden
//    }

    // 글 단건 조회
    @Test
    void getBoard_E2E() throws Exception {
        Long boardId = 7L;

        mockMvc.perform(get("/v1/boards/" + boardId)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 없는 글 조회 시 404
    @Test
    void getBoard_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/v1/boards/999999").header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andExpect(status().isNotFound());
    }


    // 글 수정
    @Test
    void updateBoard_E2E() throws Exception {
        Long boardId = 7L;
        UpdateBoardRequest request = new UpdateBoardRequest("Updated Title", "Updated Description", new Date(), new Date(), 10L, "Updated Contact Url", "Updated Contact Method");

        mockMvc.perform(patch("/v1/boards/" + boardId)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 글 삭제
    @Test
    void deleteBoard_E2E() throws Exception {
        Long boardId = 10L;

        mockMvc.perform(delete("/v1/boards/" + boardId).header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 본인 글 아닌데 수정 시 403 반환
//    @Test
//    void updateBoard_notMyPost_shouldReturn403() throws Exception {
//        Long otherBoardId = 2L; // 실제로 다른 사용자가 작성한 글 ID
//
//        UpdateBoardRequest update = new UpdateBoardRequest("Hack", "Try to hack");
//
//        mockMvc.perform(patch("/v1/boards/" + otherBoardId)
//                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0LXVzZXIiLCJyb2xlIjoiR1VFU1QifQ.dummysignature1234567890")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(update)))
//                .andExpect(status().isForbidden());
//    }


    // 글 좋아요
    @Test
    void likeBoard_E2E() throws Exception {
        Long boardId = 3L;

        mockMvc.perform(post("/v1/boards/like")
                        .param("boardId", boardId.toString())
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 두 번 좋아요하면 좋아요 취소되는 지 확인
    @Test
    void likeBoard_toggleTwice_shouldCancelLike() throws Exception {
        Long boardId = 3L;

        // 첫 번째 호출 → 찜
        mockMvc.perform(post("/v1/boards/like")
                        .param("boardId", boardId.toString())
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andExpect(status().isOk());

        // 두 번째 호출 → 찜 취소
        mockMvc.perform(post("/v1/boards/like")
                        .param("boardId", boardId.toString())
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andExpect(status().isOk());
    }


    // 글 신고
    @Test
    void reportBoard_E2E() throws Exception {
        Long boardId = 3L;

        mockMvc.perform(post("/v1/boards/report")
                        .param("boardId", boardId.toString())
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 내가 좋아요한 글 목록 조회
    @Test
    void getMyLikedBoards_E2E() throws Exception {
        mockMvc.perform(get("/v1/boards/like").header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 내가 쓴 글 조회
    @Test
    void getMyBoards_E2E() throws Exception {
        mockMvc.perform(get("/v1/boards/my").header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 게시글 목록 조회
    @Test
    void getAllBoards_E2E() throws Exception {
        mockMvc.perform(get("/v1/boards")
                        .param("keyword", "")
                        .param("type", "")
                        .param("sortType", "latest")
                        .param("page", "0")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 게시글 목록 페이징 확인
    @Test
    void getAllBoards_pagingTest() throws Exception {
        mockMvc.perform(get("/v1/boards")
                        .param("page", "0")
                        .param("sortType", "latest")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
