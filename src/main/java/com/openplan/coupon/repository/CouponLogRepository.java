package com.openplan.coupon.repository;

import com.openplan.coupon.entity.CouponLog;
import com.openplan.coupon.enums.LogType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponLogRepository extends JpaRepository<CouponLog, Long> {

    // 쿠폰 코드, 사용자 ID, 로그 타입으로 쿠폰 로그 개수를 조회
    // -> 특정 사용자가 특정 쿠폰 코드에 대해 특정 로그(예를 들면 사용)가 몇개 쌓았는지 확인
    long countByCouponCodeAndPersonIdAndLogType(String couponCode, String personId, LogType logType);

    // 사용자별 + 로그 타입별 쿠폰 로그 조회
    List<CouponLog> findByPersonIdAndLogType(String personId, LogType logType);

    // 쿠폰별 사용 로그 조회
    List<CouponLog> findByCouponCode(String couponCode);

    // 쿠폰별 + 로그 타입별 사용 로그 조회
    List<CouponLog> findByCouponCodeAndLogType(String couponCode, LogType logType);


}
