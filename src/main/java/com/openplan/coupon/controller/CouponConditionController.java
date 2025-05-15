package com.openplan.coupon.controller;

import com.openplan.coupon.dto.CouponConditionCreateRequest;
import com.openplan.coupon.dto.CouponConditionResponse;
import com.openplan.coupon.dto.CouponConditionUpdateRequest;
import com.openplan.coupon.service.CouponInfoService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/coupons/{couponId}/conditions")
@RequiredArgsConstructor
public class CouponConditionController {

    private final CouponInfoService couponInfoService;

    @PostMapping
    public ResponseEntity<CouponConditionResponse> createCouponCondition(
        @PathVariable(value = "couponId") Long couponId,
        @Valid @RequestBody CouponConditionCreateRequest couponConditionCreateRequest
    ) {
        CouponConditionResponse response = couponInfoService.addCouponCondition(couponId, couponConditionCreateRequest);

        String location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.getCouponInfoSeq())
            .toUriString();

        return ResponseEntity
            .created(URI.create(location))
            .body(response);
    }

    // todo 시간이 충분해지면 배치 추가 구현 @PostMapping("/{couponId}/conditions/batch")

    @PatchMapping("/{conditionId}")
    public ResponseEntity<CouponConditionResponse> updateCouponCondition(
        @PathVariable(value = "couponId") Long couponId,
        @PathVariable(value = "conditionId") Long conditionId,
        @Valid @RequestBody CouponConditionUpdateRequest couponConditionUpdateRequest
    ) {
        CouponConditionResponse response = couponInfoService.updateCouponCondition(couponId, conditionId, couponConditionUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{conditionId}")
    public ResponseEntity<Void> deleteCouponCondition(
        @PathVariable(value = "couponId") Long couponId,
        @PathVariable(value = "conditionId") Long conditionId
    ) {
        couponInfoService.deleteCouponCondition(couponId, conditionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{conditionId}")
    public ResponseEntity<CouponConditionResponse> getCouponCondition(
        @PathVariable(value = "couponId") Long couponId,
        @PathVariable(value = "conditionId") Long conditionId
    ) {
        CouponConditionResponse response = couponInfoService.getCouponCondition(couponId, conditionId);
        return ResponseEntity.ok(response);
    }


}
