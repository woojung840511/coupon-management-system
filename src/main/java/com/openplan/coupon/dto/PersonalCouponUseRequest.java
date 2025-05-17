package com.openplan.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "쿠폰 사용 요청")
public class PersonalCouponUseRequest {

    @NotBlank(message = "보험 계약 id는 필수입니다.")
    @Schema(description = "쿠폰을 사용할 보험 계약 ID", example = "contract123")
    private String insuranceSubscriptionDetailsId; // 보험 계약 정보 UUID

    @Schema(description = "쿠폰 사용 시 추가 데이터")
    private String useData; // JSON 데이터로 저장될 가능성이 있어 TEXT 타입으로 지정
}
