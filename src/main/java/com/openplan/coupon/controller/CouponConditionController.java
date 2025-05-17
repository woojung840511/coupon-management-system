package com.openplan.coupon.controller;

import com.openplan.coupon.config.CommonApiResponses;
import com.openplan.coupon.dto.CouponConditionCreateRequest;
import com.openplan.coupon.dto.CouponConditionResponse;
import com.openplan.coupon.dto.CouponConditionUpdateRequest;
import com.openplan.coupon.service.CouponInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "쿠폰 조건 API", description = "쿠폰에 적용되는 사용 조건을 관리하는 API")
public class CouponConditionController {

    private final CouponInfoService couponInfoService;

    @PostMapping
    @Operation(
        summary = "쿠폰 조건 생성",
        description = "쿠폰에 새로운 사용 조건을 추가합니다."
    )
    @ApiResponse(
        responseCode = "201",
        description = "조건 생성 성공",
        content = @Content(schema = @Schema(implementation = CouponConditionResponse.class))
    )
    @CommonApiResponses
    public ResponseEntity<CouponConditionResponse> createCouponCondition(
        @Parameter(description = "쿠폰 정보 ID") @PathVariable(value = "couponId") Long couponId,
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
    @Operation(
        summary = "쿠폰 조건 수정",
        description = "쿠폰 조건을 수정합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "수정 성공",
        content = @Content(schema = @Schema(implementation = CouponConditionResponse.class))
    )
    @CommonApiResponses
    public ResponseEntity<CouponConditionResponse> updateCouponCondition(
        @Parameter(description = "쿠폰 정보 ID") @PathVariable(value = "couponId") Long couponId,
        @Parameter(description = "쿠폰 조건 ID") @PathVariable(value = "conditionId") Long conditionId,
        @Valid @RequestBody CouponConditionUpdateRequest couponConditionUpdateRequest
    ) {
        CouponConditionResponse response = couponInfoService.updateCouponCondition(couponId, conditionId, couponConditionUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{conditionId}")
    @Operation(
        summary = "쿠폰 조건 삭제",
        description = "쿠폰에서 특정 조건을 삭제합니다."
    )
    @ApiResponse(
        responseCode = "204",
        description = "삭제 성공"
    )
    @CommonApiResponses
    public ResponseEntity<Void> deleteCouponCondition(
        @Parameter(description = "쿠폰 정보 ID") @PathVariable(value = "couponId") Long couponId,
        @Parameter(description = "쿠폰 조건 ID") @PathVariable(value = "conditionId") Long conditionId
    ) {
        couponInfoService.deleteCouponCondition(couponId, conditionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{conditionId}")
    @Operation(
        summary = "쿠폰 조건 조회",
        description = "특정 쿠폰의 특정 조건을 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = @Content(schema = @Schema(implementation = CouponConditionResponse.class))
    )
    @CommonApiResponses
    public ResponseEntity<CouponConditionResponse> getCouponCondition(
        @Parameter(description = "쿠폰 정보 ID") @PathVariable(value = "couponId") Long couponId,
        @Parameter(description = "쿠폰 조건 ID") @PathVariable(value = "conditionId") Long conditionId
    ) {
        CouponConditionResponse response = couponInfoService.getCouponCondition(couponId, conditionId);
        return ResponseEntity.ok(response);
    }


}
