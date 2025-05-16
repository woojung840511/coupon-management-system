package com.openplan.coupon.service;

import com.openplan.coupon.entity.CouponLog;
import com.openplan.coupon.enums.LogType;
import com.openplan.coupon.repository.CouponLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponLogService {

    private final CouponLogRepository couponLogRepository;

    public long getCouponLogCount(String couponCode, String personId, LogType logType) {
        return couponLogRepository.countByCouponCodeAndPersonIdAndLogType(couponCode, personId, logType);
    }

    public void createCouponLog(CouponLog couponLog) {
        couponLogRepository.save(couponLog);
    }
}
