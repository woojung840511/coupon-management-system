package com.openplan.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "쿠폰북 생성 요청")
public class CouponBookCreateRequest {

    @NotNull(message = "쿠폰 정보 번호는 필수입니다")
    @Schema(description = "쿠폰 정보 ID", example = "1")
    private Long couponInfoSeq;

    @NotBlank(message = "발행자 ID는 필수입니다")
    @Schema(description = "쿠폰 발행 관리자 ID", example = "admin123")
    private String adminId;

    // UNI 타입일 경우 필수 // todo 생성요청 dto를 구분해야할지 고민
    @Schema(description = "고정 쿠폰 코드 (UNI 타입인 경우 필수)", example = "WELCOME2025")
    private String fixedCouponCode;

}
