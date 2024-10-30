package com.bangtalboys.BTS_Backend.member.service;

import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository userRepository;
}
