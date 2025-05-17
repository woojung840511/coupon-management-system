package com.openplan.coupon.test;

import com.openplan.coupon.dto.CouponBookCreateRequest;
import com.openplan.coupon.dto.CouponInfoCreateRequest;
import com.openplan.coupon.dto.PersonalCouponCreateRequest;
import com.openplan.coupon.dto.PersonalCouponUseRequest;
import com.openplan.coupon.entity.InsuranceContract;
import com.openplan.coupon.entity.Person;
import com.openplan.coupon.enums.ContractStatus;
import com.openplan.coupon.enums.CouponBadgeType;
import com.openplan.coupon.enums.CouponPublishType;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.enums.PurposeType;
import com.openplan.coupon.enums.TargetType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 테스트에서 사용할 샘플 데이터를 생성하는 헬퍼 클래스
 */
public class TestDataFactory {

    // 기본 쿠폰 정보 생성 요청
    public static CouponInfoCreateRequest createDefaultCouponInfoRequest() {
        return CouponInfoCreateRequest.builder()
            .couponName("테스트 쿠폰")
            .couponType(CouponType.ONCE)
            .targetType(TargetType.B2C)
            .couponPublishType(CouponPublishType.POLY)
            .purposeType(PurposeType.RATE)
            .purposeValue("10")
            .couponBadgeType(CouponBadgeType.NEW)
            .pressCount(100)
            .useCount(0)
            .limitCount(1)
            .isAble(true)
            .isDuplicate(false)
            .couponStartAt(LocalDateTime.now())
            .couponEndAt(LocalDateTime.now().plusMonths(1))
            .build();
    }

    // 쿠폰 정보 생성 요청 2
    public static CouponInfoCreateRequest createCustomCouponInfoRequest(
        CouponType couponType,  // ONCE, MULTI, LIMIT
        Integer limitCount,
        CouponPublishType publishType,
        Integer pressCount) {

        return CouponInfoCreateRequest.builder()
            .couponName("테스트 쿠폰")
            .couponType(couponType)
            .targetType(TargetType.B2C)
            .couponPublishType(publishType)
            .purposeType(PurposeType.RATE)
            .purposeValue("10")
            .couponBadgeType(CouponBadgeType.NEW)
            .pressCount(pressCount)
            .useCount(0)
            .limitCount(limitCount)
            .isAble(true)
            .isDuplicate(false)
            .couponStartAt(LocalDateTime.now())
            .couponEndAt(LocalDateTime.now().plusMonths(1))
            .build();
    }

    // 쿠폰북 - UNI
    public static CouponBookCreateRequest createUniCouponBookRequest(Long couponInfoSeq, String fixedCouponCode) {
        return CouponBookCreateRequest.builder()
            .couponInfoSeq(couponInfoSeq)
            .fixedCouponCode(fixedCouponCode)
            .adminId("admin")
            .build();
    }

    // 쿠폰북 - POLY
    public static CouponBookCreateRequest createPolyCouponBookRequest(Long couponInfoSeq) {
        return CouponBookCreateRequest.builder()
            .couponInfoSeq(couponInfoSeq)
            .adminId("admin")
            .build();
    }

    // 사용자
    public static Person createTestPerson() {
        return Person.builder()
            .personId(UUID.randomUUID().toString())
            .name("테스트 사용자")
            .email("test@gmail.com")
            .phone("010-1234-5678")
            .build();
    }

    // 보험 계약
    public static InsuranceContract createTestContract(String personId) {
        return InsuranceContract.builder()
            .contractId(UUID.randomUUID().toString())
            .personId(personId)
            .productId(UUID.randomUUID().toString())
            .premium(new BigDecimal("50000"))
            .contractStartDate(LocalDateTime.now().minusDays(30))
            .contractEndDate(LocalDateTime.now().plusYears(1))
            .status(ContractStatus.ACTIVE)
            .build();
    }

    // 개인 쿠폰 발급 요청
    public static PersonalCouponCreateRequest createPersonalCouponRequest(String personId, String couponCode) {
        return PersonalCouponCreateRequest.builder()
            .personId(personId)
            .adminId("admin")
            .couponCode(couponCode)
            .build();
    }

    // 쿠폰 사용 요청
    public static PersonalCouponUseRequest createCouponUseRequest(String contractId) {
        return PersonalCouponUseRequest.builder()
            .insuranceSubscriptionDetailsId(contractId)
            .useData("테스트 사용 데이터")
            .build();
    }

}