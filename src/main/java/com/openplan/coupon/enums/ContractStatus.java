package com.openplan.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 계약 상태
 */
@Getter
@RequiredArgsConstructor
public enum ContractStatus {
    ACTIVE("활성"),
    EXPIRED("만료"),
    CANCELED("해지");

    private final String description;
}
