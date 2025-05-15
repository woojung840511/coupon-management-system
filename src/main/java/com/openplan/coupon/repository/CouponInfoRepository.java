package com.openplan.coupon.repository;

import com.openplan.coupon.entity.CouponInfo;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.enums.TargetType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponInfoRepository extends JpaRepository<CouponInfo, Long> {

    // 사용 가능한 쿠폰 검색
    List<CouponInfo> findByIsAbleTrue();

    // 현재 유효한 쿠폰 검색 (시작일과 종료일 기준)
    List<CouponInfo> findByIsAbleTrueAndCouponStartAtBeforeAndCouponEndAtAfter(
        LocalDateTime currentDateTime, LocalDateTime currentDateTime2);

    // 쿠폰 타입과 타겟 타입으로 검색
    List<CouponInfo> findByCouponTypeAndTargetType(CouponType couponType, TargetType targetType);
}
