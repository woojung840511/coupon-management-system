package com.openplan.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 뱃지 유형
 */
@Getter
@RequiredArgsConstructor
public enum CouponBadgeType {
    NEW("신규"),
    BEST("인기");

    private final String description;
}