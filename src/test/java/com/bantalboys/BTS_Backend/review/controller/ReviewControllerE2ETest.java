package com.bantalboys.BTS_Backend.review.controller;

import com.bangtalboys.BTS_Backend.BtsBackendApplication;
import com.bangtalboys.BTS_Backend.review.dto.request.ReviewRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(classes = BtsBackendApplication.class)
@AutoConfigureMockMvc
public class ReviewControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getOneReviews_E2E() throws Exception {
        Long reviewId = 1L;
        mockMvc.perform(get("/v1/reviews/{reviewId}", reviewId).header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllReviews_E2E() throws Exception {
        Long themeId = 1L;
        mockMvc.perform(get("/v1/reviews").param("themeId", String.valueOf(themeId)).header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void createReview_E2E() throws Exception {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setContent("test");
        reviewRequest.setPeople(1);
        reviewRequest.setTime(45);
        reviewRequest.setScareScore(3);
        reviewRequest.setActivityScore(2);
        reviewRequest.setDifficulty(3F);
        reviewRequest.setHints(2);
        reviewRequest.setVisitDate(LocalDateTime.now());
        reviewRequest.setIsSuccess(true);
        reviewRequest.setThemeId(1L);
        mockMvc.perform(post("/v1/reviews").header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void updateReview_E2E() throws Exception {
        Long reviewId = 1L;
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setContent("수정했다");
        mockMvc.perform(put("/v1/reviews/{reviewId}", reviewId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
