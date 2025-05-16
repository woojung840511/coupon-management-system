package com.openplan.coupon.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponBookCreateRequest {
    @NotNull(message = "쿠폰 정보 번호는 필수입니다")
    private Long couponInfoSeq;

    // UNI 타입일 경우 필수 // todo 생성요청 dto를 구분해야할지 고민
    private String fixedCouponCode;

    // 발급 개수 (선택적)
    private Integer pressCount;

}
