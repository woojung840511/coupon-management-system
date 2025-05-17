package com.openplan.coupon.dto;

import com.openplan.coupon.enums.CouponSortBy;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.enums.PurposeType;
import com.openplan.coupon.enums.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponFilterRequest {
    private Boolean available;              // CouponInfo (isAble = true) &&
    private Boolean used;                   // PersonalCoupon (useAt != null) -> 쿼리에서 필터링
    private Boolean expired;                // CouponBook - 만료 일시            -> 쿼리에서 필터링
    private CouponType couponType;          // CouponInfo                      -> 쿼리에서
    private PurposeType purposeType;        // CouponInfo (RATE, AMOUNT)       -> 쿼리
    private String couponNameContains;      // CouponInfo                      -> 쿼리

    private CouponSortBy sortBy;
    private SortDirection sortDirection;
}