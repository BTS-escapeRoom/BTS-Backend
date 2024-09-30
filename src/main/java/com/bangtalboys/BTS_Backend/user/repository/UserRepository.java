package com.bangtalboys.BTS_Backend.user.repository;

import com.bangtalboys.BTS_Backend.user.domain.User;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, Long socialId);
}
