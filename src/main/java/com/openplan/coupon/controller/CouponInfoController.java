package com.openplan.coupon.controller;

import com.openplan.coupon.config.CommonApiResponses;
import com.openplan.coupon.dto.CouponInfoCreateRequest;
import com.openplan.coupon.dto.CouponInfoResponse;
import com.openplan.coupon.dto.CouponInfoUpdateRequest;
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
@Tag(name = "쿠폰 정보 API", description = "쿠폰의 기본 정보를 관리하는 API")
public class CouponInfoController {

    private final CouponInfoService couponInfoService;

    @PostMapping
    @Operation(
        summary = "쿠폰 정보 생성",
        description = "새로운 쿠폰 정보를 생성합니다. 쿠폰 유형, 이름, 대상 유형, 발행 유형, 용도 등의 정보가 필요합니다."
    )
    @ApiResponse(
        responseCode = "201",
        description = "쿠폰 생성 성공",
        content = @Content(schema = @Schema(implementation = CouponInfoResponse.class))
    )
    @CommonApiResponses
    public ResponseEntity<CouponInfoResponse> createCouponInfo(
        @Valid @RequestBody CouponInfoCreateRequest request
    ) {
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
    @Operation(
        summary = "쿠폰 정보 조회",
        description = "ID로 쿠폰 정보를 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = @Content(schema = @Schema(implementation = CouponInfoResponse.class))
    )
    @CommonApiResponses
    public ResponseEntity<CouponInfoResponse> getCouponInfo(
        @Parameter(description = "쿠폰 정보 ID") @PathVariable Long id) {
        CouponInfoResponse response = couponInfoService.getCouponInfo(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @Operation(
        summary = "쿠폰 정보 수정",
        description = "쿠폰 정보를 부분 수정합니다. 변경하려는 필드만 요청에 포함하면 됩니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "수정 성공",
        content = @Content(schema = @Schema(implementation = CouponInfoResponse.class))
    )
    @CommonApiResponses
    public ResponseEntity<CouponInfoResponse> updateCouponInfo(
        @Parameter(description = "쿠폰 정보 ID") @PathVariable Long id,
        @Valid @RequestBody CouponInfoUpdateRequest request) {
        CouponInfoResponse response = couponInfoService.updateCouponInfo(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "쿠폰 정보 삭제",
        description = "쿠폰 정보를 삭제합니다."
    )
    @ApiResponse(
        responseCode = "204",
        description = "삭제 성공"
    )
    @CommonApiResponses
    public ResponseEntity<Void> deleteCouponInfo(
        @Parameter(description = "쿠폰 정보 ID") @PathVariable Long id
    ) {
        couponInfoService.deleteCouponInfo(id);
        return ResponseEntity.noContent().build();
    }
}
