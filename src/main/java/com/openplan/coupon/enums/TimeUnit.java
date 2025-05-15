package com.openplan.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 기간 단위
 */
@Getter
@RequiredArgsConstructor
public enum TimeUnit {
    YEAR("년"),
    MONTH("월"),
    DAY("일");

    private final String description;
}
