package com.openplan.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponSortBy {
    EXPIRE_AT("만료일", "expireAt"),
    COUPON_NAME("쿠폰이름", "couponName");

    private final String description;
    private final String fieldName;
}
