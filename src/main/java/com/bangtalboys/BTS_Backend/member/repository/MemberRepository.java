package com.bangtalboys.BTS_Backend.member.repository;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.bangtalboys.BTS_Backend.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // status가 ACTIVE인 회원만 조회
    @Query("SELECT m FROM Member m WHERE m.socialType = :socialType AND m.socialId = :socialId AND m.status = :status")
    Optional<Member> findBySocialTypeAndSocialIdAndStatus(@Param("socialType") SocialType socialType, @Param("socialId") String socialId, @Param("status") Status status);
    
    // 모든 상태의 회원을 조회 (탈퇴한 회원 복구 등 특수한 경우용)
    @Query("SELECT m FROM Member m WHERE m.socialType = :socialType AND m.socialId = :socialId")
    Optional<Member> findBySocialTypeAndSocialIdIgnoreStatus(@Param("socialType") SocialType socialType, @Param("socialId") String socialId);
    
    // 닉네임으로 ACTIVE 상태인 회원 조회 (닉네임 중복 체크용)
    @Query("SELECT m FROM Member m WHERE m.nickname = :nickname AND m.status = :status")
    Optional<Member> findByNicknameAndStatus(@Param("nickname") String nickname, @Param("status") Status status);
}
