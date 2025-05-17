package com.openplan.coupon.controller;

import com.openplan.coupon.config.CommonApiResponses;
import com.openplan.coupon.dto.CouponBookCreateRequest;
import com.openplan.coupon.dto.CouponBookResponse;
import com.openplan.coupon.dto.PersonalCouponResponse;
import com.openplan.coupon.service.CouponBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "쿠폰북 발행 API", description = "쿠폰북을 발행하는 API")
public class CouponBookController {

    private final CouponBookService couponBookService;

    @PostMapping
    @Operation(
        summary = "쿠폰북 발행",
        description = "쿠폰정보를 기반으로 쿠폰북을 발행합니다."
    )
    @ApiResponse(
        responseCode = "201",
        description = "쿠폰 발급 성공",
        content = @Content(schema = @Schema(implementation = PersonalCouponResponse.class))
    )
    @CommonApiResponses
    public ResponseEntity<List<CouponBookResponse>> createCouponBooks(
        @Valid @RequestBody CouponBookCreateRequest request) {
        List<CouponBookResponse> couponBooks = couponBookService.createCouponBooks(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(couponBooks);
    }

}
