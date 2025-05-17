package com.openplan.coupon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.openplan.coupon.dto.CouponBookCreateRequest;
import com.openplan.coupon.dto.CouponBookResponse;
import com.openplan.coupon.dto.CouponInfoCreateRequest;
import com.openplan.coupon.dto.CouponInfoResponse;
import com.openplan.coupon.dto.PersonalCouponCreateRequest;
import com.openplan.coupon.dto.PersonalCouponResponse;
import com.openplan.coupon.dto.PersonalCouponUseRequest;
import com.openplan.coupon.entity.InsuranceContract;
import com.openplan.coupon.entity.Person;
import com.openplan.coupon.exception.BusinessRuleException;
import com.openplan.coupon.repository.InsuranceContractRepository;
import com.openplan.coupon.repository.PersonRepository;
import com.openplan.coupon.repository.PersonalCouponRepository;
import com.openplan.coupon.test.TestDataFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PersonalCouponServiceTest {

    @Autowired
    private PersonalCouponService personalCouponService;

    @Autowired
    private CouponInfoService couponInfoService;

    @Autowired
    private CouponBookService couponBookService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private InsuranceContractRepository insuranceContractRepository;

    @Autowired
    private PersonalCouponRepository personalCouponRepository;

    private Person testPerson;
    private InsuranceContract testContract;
    private String testCouponCode;

    @BeforeEach
    void setUp() {
        testPerson = TestDataFactory.createTestPerson();
        personRepository.save(testPerson);

        testContract = TestDataFactory.createTestContract(testPerson.getPersonId());
        insuranceContractRepository.save(testContract);

        CouponInfoCreateRequest infoRequest = TestDataFactory.createDefaultCouponInfoRequest();
        CouponInfoResponse infoResponse = couponInfoService.createCouponInfo(infoRequest);

        CouponBookCreateRequest bookRequest = TestDataFactory.createPolyCouponBookRequest(infoResponse.getCouponInfoSeq());
        List<CouponBookResponse> bookResponses = couponBookService.createCouponBooks(bookRequest);
        testCouponCode = bookResponses.get(0).getCouponCode();
    }

    @Test
    @DisplayName("쿠폰 발급 성공")
    void createPersonalCoupon_Success() {
        // Given
        PersonalCouponCreateRequest request = TestDataFactory.createPersonalCouponRequest(
            testPerson.getPersonId(), testCouponCode);

        // When
        PersonalCouponResponse response = personalCouponService.createPersonalCoupon(request);

        // Then
        assertNotNull(response);
        assertEquals(testPerson.getPersonId(), response.getPersonId());
        assertEquals(testCouponCode, response.getCouponCode());
    }

    @Test
    @DisplayName("쿠폰 사용 성공")
    void usePersonalCoupon_Success() {
        // Given
        // 쿠폰 발급
        PersonalCouponCreateRequest createRequest =
            TestDataFactory.createPersonalCouponRequest(testPerson.getPersonId(), testCouponCode);
        PersonalCouponResponse createResponse = personalCouponService.createPersonalCoupon(createRequest);

        // 쿠폰 사용 요청
        PersonalCouponUseRequest useRequest = TestDataFactory.createCouponUseRequest(testContract.getContractId());

        // When
        personalCouponService.usePersonalCoupon(createResponse.getPersonCouponId(), useRequest);

        // Then
        PersonalCouponResponse updatedCoupon = personalCouponService.getPersonalCoupons(testPerson.getPersonId())
            .stream()
            .filter(c -> c.getPersonCouponId().equals(createResponse.getPersonCouponId()))
            .findFirst()
            .orElseThrow();

        // 쿠폰이 사용되었는지 확인
        assertNotNull(updatedCoupon.getUseAt());
        assertEquals(testContract.getContractId(), updatedCoupon.getInsuranceSubscriptionDetailsId());
    }

    @Test
    @DisplayName("이미 사용된 쿠폰 재사용 시 예외 발생 (CouponType.ONCE)")
    void usePersonalCoupon_AlreadyUsed() {
        // Given
        // 쿠폰 발급
        PersonalCouponCreateRequest createRequest = TestDataFactory.createPersonalCouponRequest(
            testPerson.getPersonId(), testCouponCode);
        PersonalCouponResponse createResponse = personalCouponService.createPersonalCoupon(createRequest);

        // 쿠폰 첫 번째 사용
        PersonalCouponUseRequest useRequest = TestDataFactory.createCouponUseRequest(testContract.getContractId());
        personalCouponService.usePersonalCoupon(createResponse.getPersonCouponId(), useRequest);

        // When & Then
        // 쿠폰 두 번째 사용 시도 -> 예외 발생해야 함
        assertThrows(BusinessRuleException.class, () -> {
            personalCouponService.usePersonalCoupon(createResponse.getPersonCouponId(), useRequest);
        });
    }
}