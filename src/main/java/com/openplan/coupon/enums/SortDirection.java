package com.openplan.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortDirection {
    ASC("오름차순"),
    DESC("내림차순");

    private final String description;
}
