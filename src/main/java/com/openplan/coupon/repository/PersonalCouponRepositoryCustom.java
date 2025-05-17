package com.openplan.coupon.repository;

import com.openplan.coupon.dto.CouponFilterRequest;
import com.openplan.coupon.entity.PersonalCoupon;
import java.util.List;

public interface PersonalCouponRepositoryCustom {

    List<PersonalCoupon> findByPersonIdWithFilters(String personId, CouponFilterRequest filter);
}
