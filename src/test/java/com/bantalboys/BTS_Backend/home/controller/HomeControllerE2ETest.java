package com.bantalboys.BTS_Backend.home.controller;
import com.bangtalboys.BTS_Backend.BtsBackendApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BtsBackendApplication.class)
@AutoConfigureMockMvc
public class HomeControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void home_E2E() throws Exception {
        mockMvc.perform(get("/v1/home")
                    .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzLXRva2VuIiwiaWQiOjEsInVzZXJuYW1lIjoi6rSA66as7J6QIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE3MzYyMjgzMDUsImV4cCI6ODA2MzAyMjgzMDV9.SkiUghz1aukqU2UNpUEON-N5mrQs73I1NuaoifjL0DI"))
                .andExpect(status().isOk());
    }
}
