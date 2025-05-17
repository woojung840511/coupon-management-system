package com.openplan.coupon.controller;

import com.openplan.coupon.config.CommonApiResponses;
import com.openplan.coupon.dto.CouponDetailHistoryResponse;
import com.openplan.coupon.service.CouponLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupon-logs")
@RequiredArgsConstructor
@Tag(name = "쿠폰 로그(CouponLog) 정보 API", description = "쿠폰 로그 정보를 관리하는 API")
public class CouponLogController {

    private final CouponLogService couponLogService;

    @GetMapping("/user/{personId}/details")
    @Operation(
        summary = "사용자별 쿠폰 사용 이력 조회",
        description = "특정 사용자의 쿠폰 사용 이력을 상세하게 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = @Content(schema = @Schema(implementation = CouponDetailHistoryResponse.class))
    )
    @CommonApiResponses
    public ResponseEntity<List<CouponDetailHistoryResponse>> getUserCouponDetailHistories (
        @Parameter(description = "사용자 ID") @PathVariable String personId
    ) {
        List<CouponDetailHistoryResponse> detailHistories = couponLogService.getUserCouponDetailHistories(personId);
        return ResponseEntity.ok(detailHistories);
    }


}
