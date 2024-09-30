package com.bangtalboys.BTS_Backend.config;

import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}