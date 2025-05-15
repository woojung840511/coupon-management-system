package com.openplan.coupon.entity;

import com.openplan.coupon.enums.ContractStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceContract {

    @Id
    @Column(nullable = false)
    private String contractId; // UUID

    @Column(nullable = false)
    private String personId; // 계약자(사용자) UUID

    @Column(nullable = false)
    private String productId; // 보험 상품 UUID

    @Column(nullable = false, precision = 19, scale = 4) // 정밀한 금액 표현을 위해 BigDecimal 사용
    private BigDecimal premium;

    @Column(nullable = false)
    private LocalDateTime contractStartDate;

    private LocalDateTime contractEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 생성 시간 자동 설정
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 수정 시간 자동 설정
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
