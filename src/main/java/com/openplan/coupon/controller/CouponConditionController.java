package com.openplan.coupon.controller;

import com.openplan.coupon.dto.CouponInfoCreateRequest;
import com.openplan.coupon.dto.CouponInfoResponse;
import com.openplan.coupon.dto.CouponInfoUpdateRequest;
import com.openplan.coupon.service.CouponInfoService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponConditionController {
    private final CouponInfoService couponInfoService;

    @PostMapping
    public ResponseEntity<CouponInfoResponse> createCouponInfo(@Valid @RequestBody CouponInfoCreateRequest request) {
        CouponInfoResponse response = couponInfoService.createCouponInfo(request);

        String location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.getCouponInfoSeq())
            .toUriString();

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .location(URI.create(location))
            .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponInfoResponse> getCouponInfo(@PathVariable Long id) {
        CouponInfoResponse response = couponInfoService.getCouponInfo(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CouponInfoResponse> updateCouponInfo(
        @PathVariable Long id,
        @Valid @RequestBody CouponInfoUpdateRequest request) {
        CouponInfoResponse response = couponInfoService.updateCouponInfo(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCouponInfo(@PathVariable Long id) {
        couponInfoService.deleteCouponInfo(id);
        return ResponseEntity.noContent().build();
    }
}
