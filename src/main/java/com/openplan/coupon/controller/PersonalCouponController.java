package com.openplan.coupon.controller;

import com.openplan.coupon.dto.CouponFilterRequest;
import com.openplan.coupon.dto.PersonalCouponCreateRequest;
import com.openplan.coupon.dto.PersonalCouponResponse;
import com.openplan.coupon.dto.PersonalCouponUseRequest;
import com.openplan.coupon.dto.UserCouponDetailResponse;
import com.openplan.coupon.service.PersonalCouponService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/personal-coupons")
@RequiredArgsConstructor
public class PersonalCouponController {

    private final PersonalCouponService personalCouponService;

    @PostMapping
    public ResponseEntity<PersonalCouponResponse> createPersonalCoupon(
        @Valid @RequestBody PersonalCouponCreateRequest personalCouponCreateRequest
    ) {
        PersonalCouponResponse response = personalCouponService.createPersonalCoupon(personalCouponCreateRequest);

        String location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.getPersonCouponId())
            .toUriString();

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .location(URI.create(location))
            .body(response);
    }

    @PostMapping("/{personCouponId}/use")
    public ResponseEntity<Void> usePersonalCoupon(
        @PathVariable(value = "personCouponId") String personCouponId,
        @Valid @RequestBody PersonalCouponUseRequest personalCouponUseRequest
    ) {
        personalCouponService.usePersonalCoupon(personCouponId, personalCouponUseRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/{personId}/search")
    public ResponseEntity<List<UserCouponDetailResponse>> searchUserCoupons(
        @PathVariable(value = "personId") String personId,
        @RequestBody(required = false) CouponFilterRequest filter
    ) {
        if (filter == null) {
            filter = new CouponFilterRequest();
        }

        List<UserCouponDetailResponse> filteredCoupons =
            personalCouponService.searchUserCoupons(personId, filter);
        return ResponseEntity.ok(filteredCoupons);
    }
}
