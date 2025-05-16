package com.openplan.coupon.dto;

import com.openplan.coupon.entity.CouponBook;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponBookResponse {
    private Long couponBookSeq;
    private Long couponInfoSeq;
    private String couponCode;
    private Boolean isUsed;
    private LocalDateTime expireAt;
    private String couponName; // 쿠폰 정보에서 가져옴

    public static CouponBookResponse fromEntity(CouponBook couponBook) {
        return CouponBookResponse.builder()
            .couponBookSeq(couponBook.getCouponBookSeq())
            .couponInfoSeq(couponBook.getCouponInfo().getCouponInfoSeq())
            .couponCode(couponBook.getCouponCode())
            .isUsed(couponBook.getIsUsed())
            .expireAt(couponBook.getExpireAt())
            .couponName(couponBook.getCouponInfo().getCouponName())
            .build();
    }
}
