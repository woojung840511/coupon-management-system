package com.openplan.coupon.dto;

import com.openplan.coupon.enums.CouponBadgeType;
import com.openplan.coupon.enums.CouponPublishType;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.enums.PurposeType;
import com.openplan.coupon.enums.TargetType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponInfoUpdateRequest {

    @Size(min = 2, max = 100, message = "쿠폰 이름은 2자 이상 100자 이하여야 합니다")
    private String couponName;

    private CouponType couponType;

    private TargetType targetType;

    private CouponPublishType couponPublishType;

    private PurposeType purposeType;

    private String purposeValue;

    private CouponBadgeType couponBadgeType;

    private String couponImageUrl;

    @Min(value = 0, message = "발행 수량은 0 이상이어야 합니다")
    private Integer pressCount;

    @Min(value = 0, message = "사용 수량은 0 이상이어야 합니다")
    private Integer useCount;

    @Min(value = 0, message = "제한 수량은 0 이상이어야 합니다")
    private Integer limitCount;

    private Boolean isAble;

    private Boolean isDuplicate;

    private LocalDateTime couponStartAt;

    private LocalDateTime couponEndAt;
}
