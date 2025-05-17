package com.openplan.coupon.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 사용 유형
 */
@Getter
@RequiredArgsConstructor
@Schema(description = "쿠폰 사용 유형")
public enum CouponType {
    @Schema(description = "1회 사용")
    ONCE("1회"),

    @Schema(description = "다회 사용")
    MULTI("다회"),

    @Schema(description = "제한된 횟수만큼 사용")
    LIMIT("제한");

    private final String description;
}
