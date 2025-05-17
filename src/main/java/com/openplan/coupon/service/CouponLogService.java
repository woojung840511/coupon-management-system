package com.openplan.coupon.service;

import com.openplan.coupon.dto.CouponDetailHistoryResponse;
import com.openplan.coupon.entity.CouponBook;
import com.openplan.coupon.entity.CouponInfo;
import com.openplan.coupon.entity.CouponLog;
import com.openplan.coupon.entity.Person;
import com.openplan.coupon.entity.PersonalCoupon;
import com.openplan.coupon.enums.LogType;
import com.openplan.coupon.exception.ResourceNotFoundException;
import com.openplan.coupon.repository.CouponBookRepository;
import com.openplan.coupon.repository.CouponLogRepository;
import com.openplan.coupon.repository.PersonRepository;
import com.openplan.coupon.repository.PersonalCouponRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponLogService {

    private final CouponLogRepository couponLogRepository;
    private final PersonService personService;
    // 순환 의존
//    private final CouponBookService couponBookService;
//    private final PersonalCouponService personalCouponService;
    private final CouponBookRepository couponBookRepository;
    private final PersonalCouponRepository personalCouponRepository;
    private final PersonRepository personRepository;

    public long getCouponLogCount(String couponCode, String personId, LogType logType) {
        return couponLogRepository.countByCouponCodeAndPersonIdAndLogType(couponCode, personId, logType);
    }

    public void createCouponLog(CouponLog couponLog) {
        couponLogRepository.save(couponLog);
    }

    @Transactional(readOnly = true)
    public List<CouponDetailHistoryResponse> getUserCouponDetailHistories(String personId) {
        // 사용자가 있는지 확인
        Person person = personService.getPersonEntity(personId);

        // 사용자의 개인 쿠폰 목록 조회
        List<PersonalCoupon> personalCoupons = personalCouponRepository.findByPersonId(personId);

        List<CouponDetailHistoryResponse> result = new ArrayList<>();

        for (PersonalCoupon personalCoupon : personalCoupons) {
            String couponCode = personalCoupon.getCouponCode();
            CouponBook couponBook = couponBookRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> new ResourceNotFoundException("CouponBook", "couponCode", couponCode));
            CouponInfo couponInfo = couponBook.getCouponInfo();

            // 발급자(관리자) 정보 조회
            CouponLog transferLog = couponLogRepository.findByCouponCodeAndLogType(couponCode, LogType.TRANS)
                .stream()
                .findFirst()
                .orElse(null);

            String issuerId = null;
            String issuerName = null;

            if (transferLog != null) {
                issuerId = transferLog.getPersonId();
                Optional<Person> issuerOpt = personRepository.findById(issuerId);
                issuerName = issuerOpt.map(Person::getName).orElse("관리자");
            }

            // 응답 생성
            CouponDetailHistoryResponse detailHistory = CouponDetailHistoryResponse.builder()
                .couponCode(couponCode)
                .couponName(couponInfo.getCouponName())
                .couponType(couponInfo.getCouponType())
                .limitCount(couponInfo.getLimitCount())
                .issuerId(issuerId)
                .issuerName(issuerName)
                .isUsed(personalCoupon.getUseAt() != null)
                .usedAt(personalCoupon.getUseAt())
                .usedCount(couponInfo.getUseCount())
                .insuranceContractId(personalCoupon.getInsuranceSubscriptionDetailsId())
                .useData(personalCoupon.getUseData())
                .build();

            result.add(detailHistory);
        }

        return result;
    }
}
