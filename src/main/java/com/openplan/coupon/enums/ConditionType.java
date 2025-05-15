package com.openplan.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 조건 유형
 */
@Getter
@RequiredArgsConstructor
public enum ConditionType {
    MAX_DISCOUNT("최대 할인 금액"),
    MIN_AMOUNT("최소 사용 금액"),
    ONLY_MEMBER("회원 전용"),
    ONLY_PRODUCT("상품 전용"),
    TERM("중복 사용 기간 허용"),
    EXPIRE("쿠폰 만료 정책");

    private final String description;
}
