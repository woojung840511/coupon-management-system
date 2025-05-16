package com.openplan.coupon.controller;

import com.openplan.coupon.dto.CouponBookCreateRequest;
import com.openplan.coupon.dto.CouponBookResponse;
import com.openplan.coupon.service.CouponBookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupon-books")
@RequiredArgsConstructor
public class CouponBookController {

    private final CouponBookService couponBookService;

    @PostMapping
    public ResponseEntity<List<CouponBookResponse>> createCouponBooks(
        @Valid @RequestBody CouponBookCreateRequest request) {
        List<CouponBookResponse> couponBooks = couponBookService.createCouponBooks(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(couponBooks);
    }

}
