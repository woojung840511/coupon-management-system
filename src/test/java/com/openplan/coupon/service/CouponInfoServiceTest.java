package com.openplan.coupon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.openplan.coupon.dto.CouponInfoCreateRequest;
import com.openplan.coupon.dto.CouponInfoResponse;
import com.openplan.coupon.test.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CouponInfoServiceTest {

    @Autowired
    private CouponInfoService couponInfoService;

    @Test
    @DisplayName("쿠폰 정보 생성 성공")
    void createCouponInfo_Success() {
        // Given
        CouponInfoCreateRequest request = TestDataFactory.createDefaultCouponInfoRequest();

        // When
        CouponInfoResponse response = couponInfoService.createCouponInfo(request);

        // Then
        assertNotNull(response.getCouponInfoSeq());
        assertEquals(request.getCouponName(), response.getCouponName());
    }

    @Test
    @DisplayName("쿠폰 정보 조회 성공")
    void getCouponInfo_Success() {
        // Given
        CouponInfoCreateRequest request = TestDataFactory.createDefaultCouponInfoRequest();
        CouponInfoResponse createdResponse = couponInfoService.createCouponInfo(request);

        // When
        CouponInfoResponse foundResponse = couponInfoService.getCouponInfo(createdResponse.getCouponInfoSeq());

        // Then
        assertNotNull(foundResponse);
        assertEquals(createdResponse.getCouponInfoSeq(), foundResponse.getCouponInfoSeq());
    }
}