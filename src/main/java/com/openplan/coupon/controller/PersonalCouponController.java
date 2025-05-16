package com.openplan.coupon.controller;

import com.openplan.coupon.dto.PersonalCouponCreateRequest;
import com.openplan.coupon.dto.PersonalCouponResponse;
import com.openplan.coupon.service.PersonalCouponService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
