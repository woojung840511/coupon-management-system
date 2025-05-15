package com.openplan.coupon.service;

import com.openplan.coupon.dto.CouponConditionCreateRequest;
import com.openplan.coupon.dto.CouponConditionResponse;
import com.openplan.coupon.dto.CouponConditionUpdateRequest;
import com.openplan.coupon.dto.CouponInfoCreateRequest;
import com.openplan.coupon.dto.CouponInfoResponse;
import com.openplan.coupon.dto.CouponInfoUpdateRequest;
import com.openplan.coupon.entity.CouponCondition;
import com.openplan.coupon.entity.CouponInfo;
import com.openplan.coupon.exception.ResourceNotFoundException;
import com.openplan.coupon.repository.CouponInfoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponInfoService {

    private final CouponInfoRepository couponInfoRepository;

    public CouponInfoResponse createCouponInfo(CouponInfoCreateRequest request) {
        CouponInfo savedCouponInfo = couponInfoRepository.save(request.toEntity());
        return CouponInfoResponse.fromEntity(savedCouponInfo);
    }

    @Transactional(readOnly = true)
    public CouponInfoResponse getCouponInfo(Long id) {
        CouponInfo couponInfo = getCouponInfoEntity(id);
        return CouponInfoResponse.fromEntity(couponInfo);
    }

    @Transactional(readOnly = true)
    public CouponInfo getCouponInfoEntity(Long id) { // 내부 사용 용도
        return couponInfoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("CouponInfo", "id", id));
    }

    public CouponInfoResponse updateCouponInfo(Long id, CouponInfoUpdateRequest request) {
        CouponInfo couponInfo = getCouponInfoEntity(id);

        updateCouponInfoFromRequest(request, couponInfo);

        CouponInfo updatedCouponInfo = couponInfoRepository.save(couponInfo);
        return CouponInfoResponse.fromEntity(updatedCouponInfo);
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

    public void deleteCouponInfo(Long id) {
        if (!couponInfoRepository.existsById(id)) {
            throw new ResourceNotFoundException("CouponInfo", "id", id);
        }
        couponInfoRepository.deleteById(id);
    }

    public CouponConditionResponse addCouponCondition(Long couponId, CouponConditionCreateRequest request) {
        CouponInfo couponInfo = getCouponInfoEntity(couponId);
        CouponCondition couponCondition = request.toEntity();
        couponInfo.addCondition(couponCondition);

        couponInfoRepository.save(couponInfo);

        return CouponConditionResponse.fromEntity(couponCondition);
    }

    public CouponConditionResponse updateCouponCondition(Long couponId, Long conditionId, @Valid CouponConditionUpdateRequest request) {
        CouponInfo couponInfo = getCouponInfoEntity(couponId);
        CouponCondition condition = couponInfo.getCouponCondition(conditionId);

        // todo 시간이 충분해지면 엔티티 내부로 메서드 이동
        if (request.getConditionType() != null) {
            condition.setConditionType(request.getConditionType());
        }
        if (request.getMainValue() != null) {
            condition.setMainValue(request.getMainValue());
        }
        if (request.getSubValue() != null) {
            condition.setSubValue(request.getSubValue());
        }
        if (request.getConditionDesc() != null) {
            condition.setConditionDesc(request.getConditionDesc());
        }

        couponInfoRepository.save(couponInfo);
        return CouponConditionResponse.fromEntity(condition);
    }

    public void deleteCouponCondition(Long couponId, Long conditionId) {
        CouponInfo couponInfo = getCouponInfoEntity(couponId);
        CouponCondition condition = couponInfo.getCouponCondition(conditionId);
        couponInfo.removeCondition(condition);
        couponInfoRepository.save(couponInfo);
    }


    public CouponConditionResponse getCouponCondition(Long couponId, Long conditionId) {
        CouponInfo couponInfo = getCouponInfoEntity(couponId);
        CouponCondition condition = couponInfo.getCouponCondition(conditionId);
        return CouponConditionResponse.fromEntity(condition);
    }
}
