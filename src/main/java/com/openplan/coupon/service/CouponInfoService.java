package com.openplan.coupon.service;

import com.openplan.coupon.dto.CouponInfoCreateRequest;
import com.openplan.coupon.dto.CouponInfoResponse;
import com.openplan.coupon.dto.CouponInfoUpdateRequest;
import com.openplan.coupon.entity.CouponInfo;
import com.openplan.coupon.exception.ResourceNotFoundException;
import com.openplan.coupon.repository.CouponInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponInfoService {

    private final CouponInfoRepository couponInfoRepository;

    @Transactional
    public CouponInfoResponse createCouponInfo(CouponInfoCreateRequest request) {
        CouponInfo savedCouponInfo = couponInfoRepository.save(request.toEntity());
        return CouponInfoResponse.fromEntity(savedCouponInfo);
    }

    public CouponInfoResponse getCouponInfo(Long id) {
        CouponInfo couponInfo = couponInfoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("CouponInfo", "id", id));
        return CouponInfoResponse.fromEntity(couponInfo);
    }

    @Transactional
    public CouponInfoResponse updateCouponInfo(Long id, CouponInfoUpdateRequest request) {
        CouponInfo couponInfo = couponInfoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("CouponInfo", "id", id));

        updateCouponInfoFromRequest(request, couponInfo);

        CouponInfo updatedCouponInfo = couponInfoRepository.save(couponInfo);
        return CouponInfoResponse.fromEntity(updatedCouponInfo);
    }

    @Transactional
    public void deleteCouponInfo(Long id) {
        if (!couponInfoRepository.existsById(id)) {
            throw new ResourceNotFoundException("CouponInfo", "id", id);
        }
        couponInfoRepository.deleteById(id);
    }

    // todo 시간이 충분해지면 엔티티 내부로 메서드 이동
    private static void updateCouponInfoFromRequest(CouponInfoUpdateRequest request, CouponInfo couponInfo) {
        if (request.getCouponName() != null) {
            couponInfo.setCouponName(request.getCouponName());
        }
        if (request.getCouponType() != null) {
            couponInfo.setCouponType(request.getCouponType());
        }
        if (request.getTargetType() != null) {
            couponInfo.setTargetType(request.getTargetType());
        }
        if (request.getCouponPublishType() != null) {
            couponInfo.setCouponPublishType(request.getCouponPublishType());
        }
        if (request.getPurposeType() != null) {
            couponInfo.setPurposeType(request.getPurposeType());
        }
        if (request.getPurposeValue() != null) {
            couponInfo.setPurposeValue(request.getPurposeValue());
        }

        couponInfo.setCouponBadgeType(request.getCouponBadgeType()); // 배지는 null로 설정할 수 있음

        if (request.getCouponImageUrl() != null) {
            couponInfo.setCouponImageUrl(request.getCouponImageUrl());
        }
        if (request.getPressCount() != null) {
            couponInfo.setPressCount(request.getPressCount());
        }
        if (request.getUseCount() != null) {
            couponInfo.setUseCount(request.getUseCount());
        }
        if (request.getLimitCount() != null) {
            couponInfo.setLimitCount(request.getLimitCount());
        }
        if (request.getIsAble() != null) {
            couponInfo.setIsAble(request.getIsAble());
        }
        if (request.getIsDuplicate() != null) {
            couponInfo.setIsDuplicate(request.getIsDuplicate());
        }
        if (request.getCouponStartAt() != null) {
            couponInfo.setCouponStartAt(request.getCouponStartAt());
        }
        if (request.getCouponEndAt() != null) {
            couponInfo.setCouponEndAt(request.getCouponEndAt());
        }
    }

}
