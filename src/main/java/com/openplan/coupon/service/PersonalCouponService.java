package com.openplan.coupon.service;

import com.openplan.coupon.dto.CouponConditionResponse;
import com.openplan.coupon.dto.CouponFilterRequest;
import com.openplan.coupon.dto.PersonalCouponCreateRequest;
import com.openplan.coupon.dto.PersonalCouponResponse;
import com.openplan.coupon.dto.PersonalCouponUseRequest;
import com.openplan.coupon.dto.UserCouponDetailResponse;
import com.openplan.coupon.entity.CouponBook;
import com.openplan.coupon.entity.CouponInfo;
import com.openplan.coupon.entity.CouponLog;
import com.openplan.coupon.entity.InsuranceContract;
import com.openplan.coupon.entity.Person;
import com.openplan.coupon.entity.PersonalCoupon;
import com.openplan.coupon.enums.ContractStatus;
import com.openplan.coupon.enums.CouponPublishType;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.enums.LogType;
import com.openplan.coupon.exception.ResourceNotFoundException;
import com.openplan.coupon.repository.PersonalCouponRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonalCouponService {

    private final CouponBookService couponBookService;
    private final PersonService personService;
    private final CouponLogService couponLogService;
    private final InsuranceService insuranceService;
    private final PersonalCouponRepository personalCouponRepository;


    // todo 리펙토링
    public PersonalCouponResponse createPersonalCoupon(PersonalCouponCreateRequest request) {

        String personId = request.getPersonId();
        String couponCode = request.getCouponCode();

        // 1. 사용자 존재 여부 확인
        Person person = personService.getPersonEntity(personId);
        if (person == null) {
            throw new IllegalArgumentException("사용자 ID가 유효하지 않습니다.");
        }

        // 2. 쿠폰북 존재 여부 확인
        CouponBook couponBook = couponBookService.getCouponBookEntity(couponCode);

        // 3. 쿠폰이 발급 가능한 상태인지 확인
        CouponInfo couponInfo = couponBook.getCouponInfo();
        Boolean isAble = couponInfo.getIsAble();
        if (!isAble) {
            throw new IllegalStateException("쿠폰이 발급 불가능한 상태입니다.");
        }

        // 4. 발급 가능 개수 확인
        /**
         * # 쿠폰 발행 타입에 따른 처리
         * - UNI 타입: 하나의 고정 코드만 생성 (이후 personalCoupon 발급시 pressCount 로 제한)
         *      현재까지 발급된 개수 <= pressCount
         * - POLY 타입: pressCount 만큼 고유한 난수 코드 생성
         *      현재까지 발급된 개수 <= pressCount 이지만
         *      발행된 쿠폰북 중에서 미발급된 쿠폰북만큼 발급 가능
         */
        if (couponInfo.getCouponPublishType() == CouponPublishType.UNI) {
            // UNI 타입의 경우, 발급된 쿠폰북은 1개이지만 pressCount 만큼 발급 가능
            int issuedPersonalCouponCount = personalCouponRepository.countByCouponCode(couponCode);
            if (issuedPersonalCouponCount >= couponInfo.getPressCount()) {
                throw new IllegalStateException("발급 가능한 쿠폰이 부족합니다.");
            }
        } else {
            // POLY 타입의 경우 pressCount 만큼 쿠폰북이 발급된 상태. 사용하지 않은 쿠본북이 존재한다면 발급 가능
            if (couponBook.getIsUsed()) {
                throw new IllegalStateException("이미 사용된 쿠폰북입니다.");
            }
        }

        // 5. 동일사용자에게 중복 발급 가능 여부 확인
        // isDuplicate: 발급에 관한 제한 (사용자당 같은 쿠폰을 몇 번 발급받을 수 있는가? 같은 쿠폰의 기준은 couponInfoSeq)
        Boolean isDuplicate = couponInfo.getIsDuplicate();
        if (!isDuplicate) {
            int issuedPersonalCouponCount = personalCouponRepository.countByPersonIdAndCouponInfoSeq(personId, couponInfo.getCouponInfoSeq());
            if (issuedPersonalCouponCount > 0) {
                throw new IllegalStateException("이미 발급된 쿠폰입니다.");
            }
        }

        // 6. 발급 가능 시점 확인 (종료일만 확인)
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(couponInfo.getCouponEndAt())) {
            throw new IllegalStateException("쿠폰 발급 가능 기간이 만료되었습니다.");
        }

        // 7. POLY 타입인 경우 쿠폰북 상태 업데이트
        if (couponInfo.getCouponPublishType() == CouponPublishType.POLY) {
            couponBook.use();
            couponBookService.saveCouponBook(couponBook);
        }

        // 8. 발급 로그 기록
        couponLogService.createCouponLog(
            CouponLog.ofTransfer(couponCode, request.getAdminId(), null));

        PersonalCoupon personalCoupon = PersonalCoupon.create(personId, couponCode);
        PersonalCoupon savedPersonalCoupon = personalCouponRepository.save(personalCoupon);

        return PersonalCouponResponse.fromEntity(savedPersonalCoupon);
    }

    public PersonalCoupon getPersonalCouponEntity(String personCouponId) {
        return personalCouponRepository.findById(personCouponId)
            .orElseThrow(() -> new ResourceNotFoundException("PersonalCoupon", "personCouponId", personCouponId));
    }

    // todo 리펙토링
    public void usePersonalCoupon(String personCouponId, PersonalCouponUseRequest request) {

        String contractId = request.getInsuranceSubscriptionDetailsId();

        // 1. 쿠폰 사용 여부 확인
        PersonalCoupon personalCoupon = getPersonalCouponEntity(personCouponId);
        String couponCode = personalCoupon.getCouponCode();
        CouponBook couponBook = couponBookService.getCouponBookEntity(couponCode);
        CouponInfo couponInfo = couponBook.getCouponInfo();

        // 1.1 기본 활성화 검증
        Boolean isAble = couponInfo.getIsAble();
        if (!isAble) {
            throw new IllegalStateException("쿠폰 사용 불가능한 상태입니다.");
        }

        // 1.2 쿠폰 사용 가능 기간 검증
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(couponInfo.getCouponStartAt()) || now.isAfter(couponInfo.getCouponEndAt())) {
            throw new IllegalStateException("쿠폰 사용 가능 기간이 아닙니다.");
        }

        // 1.3 쿠폰 사용 가능 개수 검증
        long usedCount = couponLogService.getCouponLogCount(couponCode, personalCoupon.getPersonId(), LogType.USE);
        CouponType couponType = couponInfo.getCouponType();
        switch (couponType) {
            case ONCE:
                if (usedCount > 0) {
                    throw new IllegalStateException("이미 사용된 쿠폰입니다.");
                }
                break;
            case LIMIT:
                if (couponInfo.getLimitCount() <= usedCount) {
                    throw new IllegalStateException("쿠폰 사용 가능 개수를 다 소진했습니다.");
                }
                break;
            case MULTI:
                break;
        }

        // 1.4 보험계약 존재 및 활성화 여부 확인
        InsuranceContract insuranceContract = insuranceService.getInsuranceContractEntity(contractId);
        if (insuranceContract.getStatus() != ContractStatus.ACTIVE) {
            throw new IllegalStateException("보험계약이 활성화 상태가 아닙니다.");
        }

        // 1.5 CouponCondition 검증 // todo

        // 2. 쿠폰 사용 처리
        personalCoupon.setUseAt(LocalDateTime.now());
        personalCoupon.setInsuranceSubscriptionDetailsId(contractId);
        personalCoupon.setUseData(request.getUseData());

        // 3. 쿠폰 정보 업데이트
        couponInfo.setUseCount(couponInfo.getUseCount() + 1);

        // 4. 쿠폰 사용 로그 생성
        couponLogService.createCouponLog(
            CouponLog.ofUsage(couponCode, personalCoupon.getPersonId(), request.getUseData()));
    }


    @Transactional(readOnly = true)
    public List<UserCouponDetailResponse> searchUserCoupons(String personId, CouponFilterRequest filter) {

        // 기본 필터 적용해서 조회
        List<PersonalCoupon> personalCoupons = personalCouponRepository.findByPersonIdWithFilters(personId, filter);
        LocalDateTime now = LocalDateTime.now();

        CouponFilterRequest finalFilter = filter;
        return personalCoupons.stream()
            .map(personalCoupon -> {
                String couponCode = personalCoupon.getCouponCode();
                CouponBook couponBook = couponBookService.getCouponBookEntity(couponCode);
                CouponInfo couponInfo = couponBook.getCouponInfo();

                int usedCount = (int) couponLogService.getCouponLogCount(couponCode, personId, LogType.USE);
                Integer limitCount = couponInfo.getLimitCount();
                Integer remainingCount = null;
                if (limitCount != null) {
                    remainingCount = Math.max(0, limitCount - usedCount);
                }

                boolean isExpired = now.isAfter(couponBook.getExpireAt());
                boolean isAvailable = calculateAvailability(
                    personalCoupon, couponInfo, isExpired, remainingCount);

                List<CouponConditionResponse> conditions = couponInfo.getConditions().stream()
                    .map(CouponConditionResponse::fromEntity)
                    .toList();

                return UserCouponDetailResponse.builder()
                    .personCouponId(personalCoupon.getPersonCouponId())
                    .couponCode(couponCode)
                    .couponName(couponInfo.getCouponName())

                    .couponType(couponInfo.getCouponType())
                    .couponTypeDesc(couponInfo.getCouponType().getDescription())
                    .purposeType(couponInfo.getPurposeType())
                    .purposeTypeDesc(couponInfo.getPurposeType().getDescription())
                    .purposeValue(couponInfo.getPurposeValue())
                    .badgeType(couponInfo.getCouponBadgeType())
                    .couponImageUrl(couponInfo.getCouponImageUrl())

                    .isUsed(personalCoupon.getUseAt() != null)
                    .useAt(personalCoupon.getUseAt())
                    .expireAt(couponBook.getExpireAt())
                    .isExpired(isExpired)
                    .isAvailable(isAvailable)

                    .usedCount(usedCount)
                    .limitCount(limitCount)
                    .remainingCount(remainingCount)

                    .conditions(conditions)
                    .insuranceSubscriptionDetailsId(personalCoupon.getInsuranceSubscriptionDetailsId())
                    .useData(personalCoupon.getUseData())
                    .build();
            })
            .filter(dto -> finalFilter.getAvailable() == null || dto.isAvailable() == finalFilter.getAvailable())
            .collect(Collectors.toList());
    }

    private boolean calculateAvailability (
        PersonalCoupon personalCoupon, CouponInfo couponInfo, boolean isExpired, Integer remainingCount ) {

        if (isExpired || !couponInfo.getIsAble()) {
            return false;
        }

        if (personalCoupon.getUseAt() != null) {
            return switch (couponInfo.getCouponType()) {
                case ONCE -> false;
                case LIMIT -> remainingCount != null && remainingCount > 0;
                case MULTI -> true;
            };

        } else {
            return true;
        }
    }
}
