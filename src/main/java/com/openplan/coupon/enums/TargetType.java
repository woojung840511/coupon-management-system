package com.openplan.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 서비스 대상 유형 - B2B, B2C 서비스 대상
 */
@Getter
@RequiredArgsConstructor
public enum TargetType {
    B2B("기업 고객"),
    B2C("개인 고객"),
    ALL("전체");

    private final String description;
}