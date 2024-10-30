package com.bangtalboys.BTS_Backend.member.repository;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
}
