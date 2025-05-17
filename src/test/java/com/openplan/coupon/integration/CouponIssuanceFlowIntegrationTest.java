package com.openplan.coupon.integration;

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
import com.openplan.coupon.entity.CouponInfo;
import com.openplan.coupon.entity.InsuranceContract;
import com.openplan.coupon.entity.Person;
import com.openplan.coupon.enums.CouponPublishType;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.repository.CouponBookRepository;
import com.openplan.coupon.repository.CouponInfoRepository;
import com.openplan.coupon.repository.InsuranceContractRepository;
import com.openplan.coupon.repository.PersonRepository;
import com.openplan.coupon.repository.PersonalCouponRepository;
import com.openplan.coupon.service.CouponBookService;
import com.openplan.coupon.service.CouponInfoService;
import com.openplan.coupon.service.PersonalCouponService;
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
public class CouponIssuanceFlowIntegrationTest {

    @Autowired
    private CouponInfoService couponInfoService;

    @Autowired
    private CouponBookService couponBookService;

    @Autowired
    private PersonalCouponService personalCouponService;

    @Autowired
    private CouponInfoRepository couponInfoRepository;

    @Autowired
    private CouponBookRepository couponBookRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private InsuranceContractRepository insuranceContractRepository;

    @Autowired
    private PersonalCouponRepository personalCouponRepository;

    private Person testPerson;
    private InsuranceContract testContract;

    @BeforeEach
    void setUp() {
        testPerson = TestDataFactory.createTestPerson();;
        personRepository.save(testPerson);

        testContract = TestDataFactory.createTestContract(testPerson.getPersonId());
        insuranceContractRepository.save(testContract);
    }

    @Test
    @DisplayName("쿠폰 발급 및 사용 전체 흐름 통합 테스트 - UNI 타입")
    void couponIssuanceAndUsageFlow_UniType() {
        // step 1. 쿠폰 정보 생성
        CouponInfoCreateRequest infoRequest = TestDataFactory.createCustomCouponInfoRequest(
            CouponType.ONCE,
            1,
            CouponPublishType.UNI,
            1
        );
        CouponInfoResponse infoResponse = couponInfoService.createCouponInfo(infoRequest);

        // step 2. 쿠폰 코드 생성 (uni 타입)
        String fixedCouponCode = "INTEGRATION_TEST_CODE";
        CouponBookCreateRequest bookRequest =
            TestDataFactory.createUniCouponBookRequest( infoResponse.getCouponInfoSeq(), fixedCouponCode );
        List<CouponBookResponse> bookResponses = couponBookService.createCouponBooks(bookRequest);

        // 쿠폰 코드가 생성되었는지 확인
        assertEquals(1, bookResponses.size());
        assertEquals(fixedCouponCode, bookResponses.get(0).getCouponCode());

        // step 3. 사용자에게 쿠폰 발급
        PersonalCouponCreateRequest personalRequest =
            TestDataFactory.createPersonalCouponRequest(testPerson.getPersonId(), fixedCouponCode);
        PersonalCouponResponse personalResponse = personalCouponService.createPersonalCoupon(personalRequest);

        // 쿠폰이 발급되었는지 확인
        assertNotNull(personalResponse);
        assertEquals(fixedCouponCode, personalResponse.getCouponCode());
        assertEquals(testPerson.getPersonId(), personalResponse.getPersonId());

        // step 4: 쿠폰 사용
        PersonalCouponUseRequest useRequest = TestDataFactory.createCouponUseRequest(testContract.getContractId());
        personalCouponService.usePersonalCoupon(personalResponse.getPersonCouponId(), useRequest);

        // 쿠폰이 사용되었는지 확인
        List<PersonalCouponResponse> userCoupons = personalCouponService.getPersonalCoupons(testPerson.getPersonId());

        assertEquals(1, userCoupons.size());
        PersonalCouponResponse usedCoupon = userCoupons.get(0);
        assertNotNull(usedCoupon.getUseAt());
        assertEquals(testContract.getContractId(), usedCoupon.getInsuranceSubscriptionDetailsId());

        // 쿠폰 정보의 사용 카운트가 증가했는지 확인
        CouponInfo updatedCouponInfo = couponInfoRepository.findById(infoResponse.getCouponInfoSeq()).orElseThrow();
        assertEquals(1, updatedCouponInfo.getUseCount());
    }

    @Test
    @DisplayName("쿠폰 발급 및 사용 전체 흐름 통합 테스트 - POLY 타입")
    void couponIssuanceAndUsageFlow_PolyType() {
        int limitCount = 2;

        // Step 1: 쿠폰 정보 생성
        CouponInfoCreateRequest infoRequest = TestDataFactory.createCustomCouponInfoRequest(
            CouponType.LIMIT,
            limitCount, // 2번 사용 가능
            CouponPublishType.POLY,
            5           // 5개 발행
        );

        CouponInfoResponse infoResponse = couponInfoService.createCouponInfo(infoRequest);

        // Step 2: 쿠폰 코드 생성 (POLY 타입)
        CouponBookCreateRequest bookRequest =
            TestDataFactory.createPolyCouponBookRequest(infoResponse.getCouponInfoSeq());
        List<CouponBookResponse> bookResponses = couponBookService.createCouponBooks(bookRequest);

        // 쿠폰 코드가 생성되었는지 확인
        assertEquals(5, bookResponses.size());
        String randomCouponCode = bookResponses.get(0).getCouponCode();

        // Step 3: 사용자에게 쿠폰 발급
        PersonalCouponCreateRequest personalRequest =
            TestDataFactory.createPersonalCouponRequest(testPerson.getPersonId(), randomCouponCode);
        PersonalCouponResponse personalResponse = personalCouponService.createPersonalCoupon(personalRequest);

        // 쿠폰이 발급되었는지 확인
        assertNotNull(personalResponse);
        assertEquals(randomCouponCode, personalResponse.getCouponCode());

        // Step 4: 쿠폰 사용
        for (int i = 1; i <= limitCount; i++) {
            // 쿠폰 사용 요청
            PersonalCouponUseRequest useRequest = TestDataFactory.createCouponUseRequest(testContract.getContractId());
            personalCouponService.usePersonalCoupon(personalResponse.getPersonCouponId(), useRequest);

            // 쿠폰이 사용되었는지 확인
            List<PersonalCouponResponse> userCoupons = personalCouponService.getPersonalCoupons(testPerson.getPersonId());
            assertEquals(1, userCoupons.size());

            PersonalCouponResponse usedCoupon = userCoupons.get(0);
            assertNotNull(usedCoupon.getUseAt());

            // 쿠폰 정보의 사용 카운트가 증가했는지 확인
            CouponInfo updatedCouponInfo = couponInfoRepository.findById(infoResponse.getCouponInfoSeq()).orElseThrow();
            assertEquals(i, updatedCouponInfo.getUseCount());
            assertEquals(1, personalCouponRepository.countByCouponCode(randomCouponCode));
        }

    }

    @Test
    @DisplayName("LIMIT 타입 쿠폰 사용 횟수 초과 시 예외 발생 테스트")
    void limitTypeCouponExceedLimitCountThrowsException() {
        CouponInfoCreateRequest infoRequest = TestDataFactory.createCustomCouponInfoRequest(
            CouponType.LIMIT,
            2, // 2번 사용 가능
            CouponPublishType.POLY,
            1
        );
        CouponInfoResponse infoResponse = couponInfoService.createCouponInfo(infoRequest);

        // 쿠폰 코드 생성
        CouponBookCreateRequest bookRequest = TestDataFactory.createPolyCouponBookRequest(infoResponse.getCouponInfoSeq());
        List<CouponBookResponse> bookResponses = couponBookService.createCouponBooks(bookRequest);
        String couponCode = bookResponses.get(0).getCouponCode();

        // 사용자에게 쿠폰 발급
        PersonalCouponCreateRequest personalRequest = TestDataFactory.createPersonalCouponRequest(
            testPerson.getPersonId(), couponCode);
        PersonalCouponResponse personalResponse = personalCouponService.createPersonalCoupon(personalRequest);

        // 쿠폰 2번 사용 (사용 가능 횟수)
        PersonalCouponUseRequest useRequest = TestDataFactory.createCouponUseRequest(testContract.getContractId());
        personalCouponService.usePersonalCoupon(personalResponse.getPersonCouponId(), useRequest);
        personalCouponService.usePersonalCoupon(personalResponse.getPersonCouponId(), useRequest);

        assertThrows(IllegalStateException.class, () ->
            personalCouponService.usePersonalCoupon(personalResponse.getPersonCouponId(), useRequest)
        );
    }
}
