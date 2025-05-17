package com.openplan.coupon.dto;

import com.openplan.coupon.entity.CouponInfo;
import com.openplan.coupon.enums.CouponBadgeType;
import com.openplan.coupon.enums.CouponPublishType;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.enums.PurposeType;
import com.openplan.coupon.enums.TargetType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponInfoCreateRequest {

    @NotNull(message = "쿠폰 유형은 필수입니다")
    private CouponType couponType;

    @NotBlank(message = "쿠폰 이름은 필수입니다")
    @Size(min = 2, max = 100, message = "쿠폰 이름은 2자 이상 100자 이하여야 합니다")
    private String couponName;

    @NotNull(message = "대상 유형은 필수입니다")
    private TargetType targetType;

    @NotNull(message = "쿠폰 발행 유형은 필수입니다")
    private CouponPublishType couponPublishType;

    @NotNull(message = "용도 유형은 필수입니다")
    private PurposeType purposeType;

    @NotBlank(message = "용도 값은 필수입니다")
    private String purposeValue;

    private CouponBadgeType couponBadgeType;

    private String couponImageUrl;

    @NotNull(message = "발행 수량은 필수입니다")
    @Min(value = 1, message = "발행 수량은 최소 1개 이상이어야 합니다")
    private Integer pressCount;

    @Builder.Default
    private Integer useCount = 0;

    private Integer limitCount;

    @NotNull(message = "사용 가능 여부는 필수입니다")
    private Boolean isAble;

    @NotNull(message = "중복 사용 가능 여부는 필수입니다")
    private Boolean isDuplicate;

    @NotNull(message = "쿠폰 적용 시작 일시는 필수입니다")
    private LocalDateTime couponStartAt;

    @NotNull(message = "쿠폰 적용 종료 일시는 필수입니다")
    private LocalDateTime couponEndAt;

    public CouponInfo toEntity() {
        return CouponInfo.builder()
            .couponType(couponType)
            .couponName(couponName)
            .targetType(targetType)
            .couponPublishType(couponPublishType)
            .purposeType(purposeType)
            .purposeValue(purposeValue)
            .couponBadgeType(couponBadgeType)
            .couponImageUrl(couponImageUrl)
            .pressCount(pressCount)
            .useCount(useCount)
            .limitCount(limitCount)
            .isAble(isAble)
            .isDuplicate(isDuplicate)
            .couponStartAt(couponStartAt)
            .couponEndAt(couponEndAt)
            .build();
    }
}
