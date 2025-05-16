package com.openplan.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰의 발행 유형 (쿠폰 코드 발급 유형)
 *
 * 해석 :
 * UNI 타입: 하나의 고정 코드만 생성 (이후 personalCoupon 발급시 pressCount 로 제한)
 * POLY 타입: pressCount 만큼 고유한 난수 코드 생성
 *
 */
@Getter
@RequiredArgsConstructor
public enum CouponPublishType {
    UNI("고정 코드"),
    POLY("임의 코드");

    private final String description;
}
