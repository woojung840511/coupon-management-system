package com.openplan.coupon.dto;

import com.openplan.coupon.entity.CouponCondition;
import com.openplan.coupon.enums.ConditionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponConditionResponse {

    private Long couponConditionSeq;
    private Long couponInfoSeq;
    private ConditionType conditionType;
    private String conditionTypeDesc;
    private String mainValue;
    private String subValue;
    private String conditionDesc;

    public static CouponConditionResponse fromEntity(CouponCondition couponCondition) {
        return CouponConditionResponse.builder()
            .couponConditionSeq(couponCondition.getCouponConditionSeq())
            .couponInfoSeq(couponCondition.getCouponInfo().getCouponInfoSeq())
            .conditionType(couponCondition.getConditionType())
            .conditionTypeDesc(couponCondition.getConditionType().getDescription())
            .mainValue(couponCondition.getMainValue())
            .subValue(couponCondition.getSubValue())
            .conditionDesc(couponCondition.getConditionDesc())
            .build();
    }
}
