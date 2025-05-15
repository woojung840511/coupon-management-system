package com.openplan.coupon.dto;

import com.openplan.coupon.entity.CouponCondition;
import com.openplan.coupon.enums.ConditionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponConditionCreateRequest {

    @NotNull(message = "조건 유형은 필수입니다")
    private ConditionType conditionType;

    @NotBlank(message = "주요 값은 필수입니다")
    private String mainValue;

    private String subValue;

    private String conditionDesc;

    public CouponCondition toEntity() {
        return CouponCondition.builder()
            .conditionType(conditionType)
            .mainValue(mainValue)
            .subValue(subValue)
            .conditionDesc(conditionDesc)
            .build();
    }
}
