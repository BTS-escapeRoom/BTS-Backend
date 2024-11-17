package com.bangtalboys.BTS_Backend.utils.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 공통 필드 상속용
@EntityListeners(AuditingEntityListener.class) // Auditing 이벤트 리스너 등록
public abstract class BaseEntity {

    @CreatedDate // 생성 시간 자동 저장
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 시간 자동 갱신
    private LocalDateTime updatedAt;

    // Getter
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}