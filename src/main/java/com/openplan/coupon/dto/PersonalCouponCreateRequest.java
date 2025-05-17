package com.openplan.coupon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalCouponCreateRequest {

    @NotBlank(message = "사용자 ID는 필수입니다")
    private String personId;

    @NotBlank(message = "관리자 ID는 필수입니다")
    private String adminId;

    @NotBlank(message = "쿠폰 코드는 필수입니다")
    private String couponCode;

}