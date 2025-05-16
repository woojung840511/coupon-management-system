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
public class PersonalCouponUseRequest {

    @NotBlank(message = "보험 계약 id는 필수입니다.")
    private String insuranceSubscriptionDetailsId; // 보험 계약 정보 UUID

    private String useData; // JSON 데이터로 저장될 가능성이 있어 TEXT 타입으로 지정
}
