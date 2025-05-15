package com.openplan.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰의 발행 유형 (쿠폰 코드 발급 유형)
 */
@Getter
@RequiredArgsConstructor
public enum CouponPublishType {
    UNI("고정 코드"),
    POLY("임의 코드");

    private final String description;
}
