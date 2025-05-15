package com.openplan.coupon.dto;

import com.openplan.coupon.enums.ConditionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponConditionUpdateRequest {

    private ConditionType conditionType;
    private String mainValue;
    private String subValue;
    private String conditionDesc;
}
