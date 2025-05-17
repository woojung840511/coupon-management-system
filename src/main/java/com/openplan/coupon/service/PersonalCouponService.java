package com.openplan.coupon.service;

import com.openplan.coupon.dto.CouponConditionResponse;
import com.openplan.coupon.dto.CouponFilterRequest;
import com.openplan.coupon.dto.PersonalCouponCreateRequest;
import com.openplan.coupon.dto.PersonalCouponResponse;
import com.openplan.coupon.dto.PersonalCouponUseRequest;
import com.openplan.coupon.dto.UserCouponDetailResponse;
import com.openplan.coupon.entity.CouponBook;
import com.openplan.coupon.entity.CouponCondition;
import com.openplan.coupon.entity.CouponInfo;
import com.openplan.coupon.entity.CouponLog;
import com.openplan.coupon.entity.InsuranceContract;
import com.openplan.coupon.entity.Person;
import com.openplan.coupon.entity.PersonalCoupon;
import com.openplan.coupon.enums.ConditionType;
import com.openplan.coupon.enums.ContractStatus;
import com.openplan.coupon.enums.CouponPublishType;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.enums.LogType;
import com.openplan.coupon.exception.BusinessRuleException;
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

    public PersonalCoupon getPersonalCouponEntity(String personCouponId) {
        return personalCouponRepository.findById(personCouponId)
            .orElseThrow(() -> new ResourceNotFoundException("PersonalCoupon", "personCouponId", personCouponId));
    }

    public PersonalCouponResponse createPersonalCoupon(PersonalCouponCreateRequest request) {

        String personId = request.getPersonId();
        String couponCode = request.getCouponCode();
        CouponBook couponBook = couponBookService.getCouponBookEntity(couponCode);
        CouponInfo couponInfo = couponBook.getCouponInfo();

        validateBeforePersonalCouponCreation(personId, couponInfo, couponCode, couponBook);

        // POLY 타입인 경우 쿠폰북 상태 업데이트
        if (couponInfo.getCouponPublishType() == CouponPublishType.POLY) {
            couponBook.use();
            couponBookService.saveCouponBook(couponBook);
        }

        // 발급 로그 기록
        couponLogService.createCouponLog(
            CouponLog.ofTransfer(couponCode, request.getAdminId(), null));

        // 개인 쿠폰 생성 및 저장
        PersonalCoupon personalCoupon = PersonalCoupon.create(personId, couponCode);
        PersonalCoupon savedPersonalCoupon = personalCouponRepository.save(personalCoupon);

        return PersonalCouponResponse.fromEntity(savedPersonalCoupon);
    }

    private void validateBeforePersonalCouponCreation(String personId, CouponInfo couponInfo, String couponCode, CouponBook couponBook) {
        // 사용자 존재 여부 확인 - 메서드 내부에서 예외를 발생시킴
        Person person = personService.getPersonEntity(personId);

        // 쿠폰이 발급 가능한 상태인지 확인
        if (!couponInfo.isActive()) {
            throw new BusinessRuleException("쿠폰이 발급 불가능한 상태입니다.");
        }

        // 발급 가능 시점 확인 (종료일만 확인)
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(couponInfo.getCouponEndAt())) {
            throw new BusinessRuleException("쿠폰 발급 가능 기간이 만료되었습니다.");
        }

        // 4. 발급 가능 개수 확인
        /*
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
                throw new BusinessRuleException("발급 가능한 쿠폰이 부족합니다.");
            }
        } else {
            // POLY 타입의 경우 pressCount 만큼 쿠폰북이 발급된 상태. 사용하지 않은 쿠본북이 존재한다면 발급 가능
            if (couponBook.getIsUsed()) {
                throw new BusinessRuleException("이미 사용된 쿠폰북입니다.");
            }
        }

        // 동일사용자에게 중복 발급 가능 여부 확인
        // isDuplicate: 발급에 관한 제한 (사용자당 같은 쿠폰을 몇 번 발급받을 수 있는가? 같은 쿠폰의 기준은 couponInfoSeq)
        if (!couponInfo.getIsDuplicate()) {
            int issuedPersonalCouponCount = personalCouponRepository.countByPersonIdAndCouponInfoSeq(personId, couponInfo.getCouponInfoSeq());
            if (issuedPersonalCouponCount > 0) {
                throw new BusinessRuleException("이미 발급된 쿠폰입니다.");
            }
        }
    }

    public void usePersonalCoupon(String personCouponId, PersonalCouponUseRequest request) {

        String contractId = request.getInsuranceSubscriptionDetailsId();

        // 쿠폰 조회
        PersonalCoupon personalCoupon = getPersonalCouponEntity(personCouponId);
        String couponCode = personalCoupon.getCouponCode();
        CouponBook couponBook = couponBookService.getCouponBookEntity(couponCode);
        CouponInfo couponInfo = couponBook.getCouponInfo();

        // 쿠폰 사용 방식 검증
        validateCouponUsage(couponInfo, couponCode, personalCoupon);

        // 보험계약 존재 및 활성화 여부 확인
        InsuranceContract insuranceContract = insuranceService.getInsuranceContractEntity(contractId);
        validateInsuranceContract(insuranceContract);

        // CouponCondition 검증
        validateCouponConditions(couponInfo, personalCoupon, insuranceContract);

        // 쿠폰 사용 처리
        personalCoupon.use(contractId, request.getUseData());

        // 쿠폰 정보 업데이트
        couponInfo.incrementUseCount();

        // 쿠폰 사용 로그 생성
        couponLogService.createCouponLog(
            CouponLog.ofUsage(couponCode, personalCoupon.getPersonId(), request.getUseData()));
    }

    private void validateCouponUsage(CouponInfo couponInfo, String couponCode, PersonalCoupon personalCoupon) {
        // 1.1 기본 활성화 검증
        if (!couponInfo.isActive()) {
            throw new BusinessRuleException("쿠폰 사용 불가능한 상태입니다.");
        }

        // 1.2 쿠폰 사용 가능 기간 검증
        LocalDateTime now = LocalDateTime.now();
        if (!couponInfo.isWithinUsagePeriod(now)) {
            throw new BusinessRuleException("쿠폰 사용 가능 기간이 아닙니다.");
        }

        // 1.3 쿠폰 사용 가능 개수 검증
        validateRemainingUsageCount(couponInfo, couponCode, personalCoupon);
    }

    private void validateRemainingUsageCount(CouponInfo couponInfo, String couponCode, PersonalCoupon personalCoupon) {
        long usedCount = couponLogService.getCouponLogCount(couponCode, personalCoupon.getPersonId(), LogType.USE);
        CouponType couponType = couponInfo.getCouponType();
        switch (couponType) {
            case ONCE:
                if (usedCount > 0) {
                    throw new BusinessRuleException("이미 사용된 쿠폰입니다.");
                }
                break;
            case LIMIT:
                if (couponInfo.getLimitCount() <= usedCount) {
                    throw new BusinessRuleException("쿠폰 사용 가능 개수를 다 소진했습니다.");
                }
                break;
            case MULTI:
                break;
        }
    }

    private static void validateInsuranceContract(InsuranceContract insuranceContract) {
        if (insuranceContract.getStatus() != ContractStatus.ACTIVE) {
            throw new BusinessRuleException("보험계약이 활성화 상태가 아닙니다.");
        }
    }

    private void validateCouponConditions(CouponInfo couponInfo, PersonalCoupon personalCoupon, InsuranceContract contract) {
        List<CouponCondition> conditions = couponInfo.getConditions();
        if (conditions == null || conditions.isEmpty()) {
            return;
        }

        for (CouponCondition condition : conditions) {
            validateCouponCondition(condition, personalCoupon, contract);
        }
    }

    private void validateCouponCondition(CouponCondition condition, PersonalCoupon personalCoupon, InsuranceContract contract) {
        ConditionType conditionType = condition.getConditionType();
        String mainValue = condition.getMainValue();
        String subValue = condition.getSubValue();

        switch (conditionType) {
            case MAX_DISCOUNT: // 최대 할인 금액
                break;
            case MIN_AMOUNT:    // 최소 사용 금액
                break;
            case ONLY_MEMBER:   // 회원 전용
                break;
            case ONLY_PRODUCT:
                break;
            case TERM:
                break;
            case EXPIRE:
                break;

        }
    }

    // todo 리펙토링
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

    public List<PersonalCouponResponse> getPersonalCoupons(String personId) {
        return personalCouponRepository.findByPersonId(personId)
            .stream()
            .map(PersonalCouponResponse::fromEntity)
            .collect(Collectors.toList());
    }
}
