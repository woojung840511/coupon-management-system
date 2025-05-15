package com.openplan.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 사용 유형
 */
@Getter
@RequiredArgsConstructor
public enum CouponType {
    ONCE("1회"),
    MULTI("다회"),
    LIMIT("제한");

    private final String description;
}
