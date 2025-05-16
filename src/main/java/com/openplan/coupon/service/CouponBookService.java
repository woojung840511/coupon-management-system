package com.openplan.coupon.service;

import com.openplan.coupon.dto.CouponBookCreateRequest;
import com.openplan.coupon.dto.CouponBookResponse;
import com.openplan.coupon.entity.CouponBook;
import com.openplan.coupon.entity.CouponInfo;
import com.openplan.coupon.enums.CouponPublishType;
import com.openplan.coupon.exception.BusinessRuleException;
import com.openplan.coupon.repository.CouponBookRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponBookService {

    private final CouponInfoService couponInfoService;
    private final CouponBookRepository couponBookRepository;
    // todo codeGenerator 추가

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<CouponBookResponse> createCouponBooks(CouponBookCreateRequest request) {
        CouponInfo couponInfo = couponInfoService.getCouponInfoEntity(request.getCouponInfoSeq());

        // 이미 쿠폰북이 생성되었는지 확인
        validateCouponBookCreatable(couponInfo);

        // 쿠폰 발행 타입에 따른 처리
        /*
         * UNI 타입: 하나의 고정 코드만 생성 (이후 personalCoupon 발급시 pressCount 로 제한)
         * POLY 타입: pressCount 만큼 고유한 난수 코드 생성
         */
        List<CouponBook> couponBooks;
        if (couponInfo.getCouponPublishType() == CouponPublishType.UNI) {
            couponBooks = createUniTypeCouponBooks(couponInfo, request.getFixedCouponCode());
        } else {
            couponBooks = createPolyTypeCouponBooks(couponInfo);
        }

        // 생성된 쿠폰북 저장
        List<CouponBook> savedCouponBooks = couponBookRepository.saveAll(couponBooks);

        return savedCouponBooks.stream()
            .map(CouponBookResponse::fromEntity)
            .collect(Collectors.toList());
    }

    private void validateCouponBookCreatable(CouponInfo couponInfo) {
        long existingCount = couponBookRepository.countByCouponInfo_CouponInfoSeq(couponInfo.getCouponInfoSeq());
        if (existingCount > 0) {
            throw new BusinessRuleException("이미 쿠폰북이 생성되었습니다. 쿠폰 정보 ID: " + couponInfo.getCouponInfoSeq());
        }
    }

    private List<CouponBook> createUniTypeCouponBooks(CouponInfo couponInfo, String fixedCouponCode) {
        if (fixedCouponCode == null) {
            throw new BusinessRuleException("고정 코드(UNI) 쿠폰은 쿠폰 코드가 필수입니다.");
        }

        if (couponBookRepository.existsByCouponCode(fixedCouponCode)) {
            throw new BusinessRuleException("이미 존재하는 쿠폰 코드입니다: " + fixedCouponCode);
        }

        CouponBook couponBook = new CouponBook(couponInfo, fixedCouponCode);
        return Collections.singletonList(couponBook);
    }

    private List<CouponBook> createPolyTypeCouponBooks(CouponInfo couponInfo) {
        List<CouponBook> couponBooks = new ArrayList<>();
        int pressCount = couponInfo.getPressCount();

        for (int i = 0; i < pressCount; i++) {
            String randomCode = generatePolyCouponCode();
            CouponBook couponBook = new CouponBook(couponInfo, randomCode);
            couponBooks.add(couponBook);
        }

        return couponBooks;
    }

    private String generatePolyCouponCode() {
        String randomCode;
        boolean isExistingCode;

        do {
            randomCode = UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, 16)
                .toUpperCase();

            isExistingCode = couponBookRepository.existsByCouponCode(randomCode);
        } while (isExistingCode);

        return randomCode;
    }
}
