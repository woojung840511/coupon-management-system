package com.openplan.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 용도 유형
 */
@Getter
@RequiredArgsConstructor
public enum PurposeType {
    RATE("할인율"),
    AMOUNT("차감 비용");

    private final String description;
}
