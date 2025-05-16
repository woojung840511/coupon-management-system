package com.openplan.coupon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.openplan.coupon.dto.CouponBookCreateRequest;
import com.openplan.coupon.dto.CouponBookResponse;
import com.openplan.coupon.entity.CouponInfo;
import com.openplan.coupon.enums.CouponPublishType;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.enums.PurposeType;
import com.openplan.coupon.enums.TargetType;
import com.openplan.coupon.exception.BusinessRuleException;
import com.openplan.coupon.repository.CouponBookRepository;
import com.openplan.coupon.repository.CouponInfoRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CouponBookServiceTest {

    @Autowired
    private CouponBookService couponBookService;

    @Autowired
    private CouponBookRepository couponBookRepository;

    @Autowired
    private CouponInfoRepository couponInfoRepository;

    private CouponInfo uniTypeCouponInfo;
    private CouponInfo polyTypeCouponInfo;

    @BeforeEach
    void setUp() {
        // UNI 타입 쿠폰 정보 생성 및 저장
        uniTypeCouponInfo = CouponInfo.builder()
            .couponName("통합 테스트 고정 쿠폰")
            .couponType(CouponType.ONCE)
            .targetType(TargetType.ALL)
            .couponPublishType(CouponPublishType.UNI)
            .purposeType(PurposeType.AMOUNT)
            .purposeValue("1000")
            .pressCount(1)
            .useCount(0)
            .limitCount(null)
            .isAble(true)
            .isDuplicate(false)
            .couponStartAt(LocalDateTime.now())
            .couponEndAt(LocalDateTime.now().plusMonths(1))
            .build();

        uniTypeCouponInfo = couponInfoRepository.save(uniTypeCouponInfo);

        // POLY 타입 쿠폰 정보 생성 및 저장
        polyTypeCouponInfo = CouponInfo.builder()
            .couponName("통합 테스트 랜덤 쿠폰")
            .couponType(CouponType.ONCE)
            .targetType(TargetType.ALL)
            .couponPublishType(CouponPublishType.POLY)
            .purposeType(PurposeType.AMOUNT)
            .purposeValue("1000")
            .pressCount(3) // 3개 발행
            .useCount(0)
            .limitCount(null)
            .isAble(true)
            .isDuplicate(false)
            .couponStartAt(LocalDateTime.now())
            .couponEndAt(LocalDateTime.now().plusMonths(1))
            .build();

        polyTypeCouponInfo = couponInfoRepository.save(polyTypeCouponInfo);
    }

    @Test
    @DisplayName("UNI 타입 쿠폰북 생성  테스트")
    void testCreateUniTypeCouponBook() {
        // Given
        CouponBookCreateRequest request = CouponBookCreateRequest.builder()
            .couponInfoSeq(uniTypeCouponInfo.getCouponInfoSeq())
            .fixedCouponCode("CODE123")
            .adminId("admin")
            .build();

        // When
        List<CouponBookResponse> responses = couponBookService.createCouponBooks(request);

        // Then
        assertEquals(1, responses.size());
        assertEquals("CODE123", responses.get(0).getCouponCode());
    }

    @Test
    @DisplayName("POLY 타입 쿠폰북 생성 테스트")
    void testCreatePolyTypeCouponBook() {
        // Given
        CouponBookCreateRequest request = CouponBookCreateRequest.builder()
            .couponInfoSeq(polyTypeCouponInfo.getCouponInfoSeq())
            .adminId("admin")
            .build();

        // When
        List<CouponBookResponse> responses = couponBookService.createCouponBooks(request);

        // Then
        assertEquals(3, responses.size());
        assertEquals(3, couponBookRepository.countByCouponInfo_CouponInfoSeq(polyTypeCouponInfo.getCouponInfoSeq()));
    }

    @Test
    @DisplayName("이미 생성된 쿠폰북에 대한 중복 생성 시도 통합 테스트")
    void testDuplicateCouponBookCreation() {
        // Given
        CouponBookCreateRequest request = CouponBookCreateRequest.builder()
            .couponInfoSeq(uniTypeCouponInfo.getCouponInfoSeq())
            .fixedCouponCode("INTEGRATION123")
            .adminId("admin")
            .build();

        // 첫 번째 생성
        couponBookService.createCouponBooks(request);

        // Then
        // 두 번째 생성 시도 시 예외 발생 확인
        assertThrows(BusinessRuleException.class, () ->
            couponBookService.createCouponBooks(request));
    }
}