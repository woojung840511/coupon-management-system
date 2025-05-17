package com.openplan.coupon.dto;

import com.openplan.coupon.enums.CouponType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDetailHistoryResponse {

    private String couponCode;
    private String couponName;
    private CouponType couponType;
    private Integer limitCount;

    // 발급 정보
    private String issuerId; // 발급자 ID
    private String issuerName; // 발급자 이름

    // 사용 정보
    private boolean isUsed;       // 사용 여부
    private LocalDateTime usedAt; // 사용 시간
    private Integer usedCount;
    private String insuranceContractId; // 보험 계약 ID
    private String useData; // 사용 데이터
}
