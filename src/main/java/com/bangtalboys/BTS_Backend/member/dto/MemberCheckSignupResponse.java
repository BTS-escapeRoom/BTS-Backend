package com.bangtalboys.BTS_Backend.member.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberCheckSignupResponse {
    private boolean exists;
    private Long memberId;
}
