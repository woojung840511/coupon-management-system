package com.openplan.coupon.dto;

import com.openplan.coupon.entity.PersonalCoupon;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalCouponResponse {

    private String personCouponId;
    private String personId;
    private String couponCode;
    private LocalDateTime useAt;
    private String insuranceSubscriptionDetailsId;
    private String useData;

    public static PersonalCouponResponse fromEntity(PersonalCoupon personalCoupon) {
        return PersonalCouponResponse.builder()
            .personCouponId(personalCoupon.getPersonCouponId())
            .personId(personalCoupon.getPersonId())
            .couponCode(personalCoupon.getCouponCode())
            .useAt(personalCoupon.getUseAt())
            .insuranceSubscriptionDetailsId(personalCoupon.getInsuranceSubscriptionDetailsId())
            .useData(personalCoupon.getUseData())
            .build();
    }
}
