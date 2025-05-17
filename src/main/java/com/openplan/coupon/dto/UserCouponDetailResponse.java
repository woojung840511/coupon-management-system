package com.openplan.coupon.dto;

import com.openplan.coupon.enums.CouponBadgeType;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.enums.PurposeType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCouponDetailResponse {

    private String personCouponId;        // PersonalCoupon
    private String couponCode;            // PersonalCoupon
    private String couponName;            // CouponInfo

    // CouponInfo
    private CouponType couponType;        // 쿠폰 유형 (ONCE, MULTI, LIMIT)
    private String couponTypeDesc;
    private PurposeType purposeType;      // 용도 유형 (RATE, AMOUNT)
    private String purposeTypeDesc;
    private String purposeValue;          // 할인율 또는 금액
    private CouponBadgeType badgeType;    // 배지 유형
    private String couponImageUrl;        // 쿠폰 이미지

    private boolean isUsed;               // PersonalCoupon (useAt != null)
    private LocalDateTime useAt;          // PersonalCoupon - 사용 일시
    private LocalDateTime expireAt;       // CouponBook - 만료 일시
    private boolean isExpired;            // 계산값 (LocalDateTime.now().isAfter(expireAt))
    private boolean isAvailable;          // 계산값 (복합 조건 - CouponInfo.isAble = true && !isUsed && !isExpired && 사용횟수와 제한횟수 비교)

    private Integer usedCount;            // CouponLog (count by couponCode + personId + LogType.USE)
    private Integer limitCount;           // CouponInfo - 사용 제한 횟수
    private Integer remainingCount;       // 계산값 (limitCount - usedCount)

    private List<CouponConditionResponse> conditions; // CouponInfo -> conditions

    private String insuranceSubscriptionDetailsId;  // PersonalCoupon - 사용한 보험 계약 정보 ID
    private String useData;                         // PersonalCoupon - 사용 데이터

}
