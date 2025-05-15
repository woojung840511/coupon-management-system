package com.openplan.coupon.config;

import com.openplan.coupon.entity.CouponCondition;
import com.openplan.coupon.entity.CouponInfo;
import com.openplan.coupon.entity.InsuranceContract;
import com.openplan.coupon.entity.InsuranceProduct;
import com.openplan.coupon.entity.Person;
import com.openplan.coupon.enums.ConditionType;
import com.openplan.coupon.enums.ContractStatus;
import com.openplan.coupon.enums.CouponBadgeType;
import com.openplan.coupon.enums.CouponPublishType;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.enums.PurposeType;
import com.openplan.coupon.enums.TargetType;
import com.openplan.coupon.repository.CouponInfoRepository;
import com.openplan.coupon.repository.InsuranceContractRepository;
import com.openplan.coupon.repository.InsuranceProductRepository;
import com.openplan.coupon.repository.PersonRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SampleDataInitializer {

    @Bean
    // // 학습: CommandLineRunner는 Spring Boot 애플리케이션이 시작될 때 특정 코드를 실행할 수 있게 해주는 기능적 인터페이스(Functional Interface)
    public CommandLineRunner initSampleData(
        PersonRepository personRepository,
        InsuranceProductRepository insuranceProductRepository,
        InsuranceContractRepository insuranceContractRepository,
        CouponInfoRepository couponInfoRepository
    ) {

        return args -> { // args는 애플리케이션 실행 시 전달된 명령줄 인자들

            // 사용자 샘플 데이터 생성
            Person person1 = Person.builder()
                .personId(UUID.randomUUID().toString())
                .name("노")
                .email("roh@example.com")
                .phone("010-1234-5671")
                .build();

            Person person2 = Person.builder()
                .personId(UUID.randomUUID().toString())
                .name("우")
                .email("woo@example.com")
                .phone("010-1234-5672")
                .build();

            Person person3 = Person.builder()
                .personId(UUID.randomUUID().toString())
                .name("정")
                .email("jung@example.com")
                .phone("010-1234-5673")
                .build();

            personRepository.save(person1);
            personRepository.save(person2);
            personRepository.save(person3);

            // 보험 상품 샘플 데이터 생성
            InsuranceProduct product1 = InsuranceProduct.builder()
                .productId(UUID.randomUUID().toString())
                .productName("착!easy암보험")
                .premium(new BigDecimal("50000"))
                .build();

            InsuranceProduct product2 = InsuranceProduct.builder()
                .productId(UUID.randomUUID().toString())
                .productName("1·2·3인실 입원비 플랜(건강)")
                .premium(new BigDecimal("30000"))
                .build();

            insuranceProductRepository.save(product1);
            insuranceProductRepository.save(product2);

            // 보험 계약 샘플 데이터 생성
            InsuranceContract contract1 = InsuranceContract.builder()
                .contractId(UUID.randomUUID().toString())
                .personId(person1.getPersonId())
                .productId(product1.getProductId())
                .premium(product1.getPremium().add(new BigDecimal("10000"))) // 추가 보험료 10,000원
                .contractStartDate(LocalDateTime.now().minusMonths(3))
                .contractEndDate(LocalDateTime.now().plusYears(1))
                .status(ContractStatus.ACTIVE)
                .build();

            insuranceContractRepository.save(contract1);

            // 쿠폰 정보 샘플 데이터 생성
            CouponInfo couponInfo1 = CouponInfo.builder()
                .couponName("신규 가입 축하 쿠폰")
                .couponType(CouponType.ONCE)
                .targetType(TargetType.B2C)
                .couponPublishType(CouponPublishType.POLY)
                .purposeType(PurposeType.RATE)
                .purposeValue("10") // 10% 할인
                .couponBadgeType(CouponBadgeType.NEW)
                .pressCount(100)
                .useCount(0)
                .limitCount(1)
                .isAble(true)
                .isDuplicate(false)
                .couponStartAt(LocalDateTime.now())
                .couponEndAt(LocalDateTime.now().plusMonths(1))
                .build();

            CouponInfo couponInfo2 = CouponInfo.builder()
                .couponName("VIP 고객 감사 쿠폰")
                .couponType(CouponType.MULTI)
                .targetType(TargetType.B2B)
                .couponPublishType(CouponPublishType.UNI)
                .purposeType(PurposeType.AMOUNT)
                .purposeValue("5000") // 5,000원 할인
                .couponBadgeType(CouponBadgeType.BEST)
                .pressCount(50)
                .useCount(0)
                .limitCount(3)
                .isAble(true)
                .isDuplicate(true)
                .couponStartAt(LocalDateTime.now())
                .couponEndAt(LocalDateTime.now().plusMonths(3))
                .build();

            couponInfoRepository.save(couponInfo1);
            couponInfoRepository.save(couponInfo2);

            // 쿠폰 조건 추가
            CouponCondition condition1 = CouponCondition.builder()
                .conditionType(ConditionType.MIN_AMOUNT)
                .mainValue("10000") // 최소 10,000원 이상 결제 시
                .conditionDesc("10,000원 이상 결제시 사용 가능")
                .build();

            CouponCondition condition2 = CouponCondition.builder()
                .conditionType(ConditionType.MAX_DISCOUNT)
                .mainValue("50000") // 최대 50,000원 할인
                .conditionDesc("최대 할인 금액 50,000원")
                .build();

            couponInfo1.addCondition(condition1);
            couponInfo2.addCondition(condition2);

            couponInfoRepository.save(couponInfo1);
            couponInfoRepository.save(couponInfo2);

            log.info("샘플 데이터가 성공적으로 초기화되었습니다.");
        };
    }

}
