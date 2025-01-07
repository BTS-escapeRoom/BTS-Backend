package com.bangtalboys.BTS_Backend.utils.enums;

import lombok.Getter;

@Getter
public enum Token {
    AccessToken("access-token", 600000L),
    RefreshToken("refresh-token", 86400000L);

    private final String Type;
    private final Long Ttl;

    Token(String type, long ttl) {
        this.Type = type;
        this.Ttl = ttl;
    }
}
