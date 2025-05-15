package com.openplan.coupon.dto;

import com.openplan.coupon.entity.CouponInfo;
import com.openplan.coupon.enums.CouponBadgeType;
import com.openplan.coupon.enums.CouponPublishType;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.enums.PurposeType;
import com.openplan.coupon.enums.TargetType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponInfoResponse {

    private Long couponInfoSeq;
    private String couponName;
    private CouponType couponType;
    private String couponTypeDesc;
    private TargetType targetType;
    private String targetTypeDesc;
    private CouponPublishType couponPublishType;
    private String couponPublishTypeDesc;
    private PurposeType purposeType;
    private String purposeTypeDesc;
    private String purposeValue;
    private CouponBadgeType couponBadgeType;
    private String couponBadgeTypeDesc;
    private String couponImageUrl;
    private Integer pressCount;
    private Integer useCount;
    private Integer limitCount;
    private Boolean isAble;
    private Boolean isDuplicate;
    private LocalDateTime couponStartAt;
    private LocalDateTime couponEndAt;
    private List<CouponConditionResponse> conditions;

    public static CouponInfoResponse fromEntity(CouponInfo couponInfo) {
        CouponInfoResponseBuilder builder = CouponInfoResponse.builder()
            .couponInfoSeq(couponInfo.getCouponInfoSeq())
            .couponName(couponInfo.getCouponName())
            .couponType(couponInfo.getCouponType())
            .couponTypeDesc(couponInfo.getCouponType().getDescription())
            .targetType(couponInfo.getTargetType())
            .targetTypeDesc(couponInfo.getTargetType().getDescription())
            .couponPublishType(couponInfo.getCouponPublishType())
            .couponPublishTypeDesc(couponInfo.getCouponPublishType().getDescription())
            .purposeType(couponInfo.getPurposeType())
            .purposeTypeDesc(couponInfo.getPurposeType().getDescription())
            .purposeValue(couponInfo.getPurposeValue())
            .couponImageUrl(couponInfo.getCouponImageUrl())
            .pressCount(couponInfo.getPressCount())
            .useCount(couponInfo.getUseCount())
            .limitCount(couponInfo.getLimitCount())
            .isAble(couponInfo.getIsAble())
            .isDuplicate(couponInfo.getIsDuplicate())
            .couponStartAt(couponInfo.getCouponStartAt())
            .couponEndAt(couponInfo.getCouponEndAt());

        if (couponInfo.getCouponBadgeType() != null) {
            builder.couponBadgeType(couponInfo.getCouponBadgeType())
                .couponBadgeTypeDesc(couponInfo.getCouponBadgeType().getDescription());
        }

        if (couponInfo.getConditions() != null) {
            builder.conditions(couponInfo.getConditions().stream()
                .map(CouponConditionResponse::fromEntity)
                .collect(Collectors.toList()));
        }

        return builder.build();
    }
}