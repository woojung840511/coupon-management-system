package com.openplan.coupon.repository;

import com.openplan.coupon.entity.CouponLog;
import com.openplan.coupon.enums.LogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponLogRepository extends JpaRepository<CouponLog, Long> {

    // 쿠폰 코드, 사용자 ID, 로그 타입으로 쿠폰 로그 개수를 조회
    // -> 특정 사용자가 특정 쿠폰 코드에 대해 특정 로그(예를 들면 사용)가 몇개 쌓았는지 확인
    long countByCouponCodeAndPersonIdAndLogType(String couponCode, String personId, LogType logType);
}
