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
@Schema(description = "사용자 쿠폰 발급 요청")
public class PersonalCouponCreateRequest {

    @NotBlank(message = "사용자 ID는 필수입니다")
    @Schema(description = "쿠폰을 발급받을 사용자 ID", example = "user123")
    private String personId;

    @NotBlank(message = "관리자 ID는 필수입니다")
    @Schema(description = "쿠폰을 발급하는 관리자 ID", example = "admin123")
    private String adminId;

    @NotBlank(message = "쿠폰 코드는 필수입니다")
    @Schema(description = "발급할 쿠폰 코드", example = "WELCOME2025")
    private String couponCode;

}