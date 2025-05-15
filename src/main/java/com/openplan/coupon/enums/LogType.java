package com.openplan.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 로그 유형
 */
@Getter
@RequiredArgsConstructor
public enum LogType {
    PUBLISH("발행"),
    USE("사용"),
    TRANS("전송");

    private final String description;
}
