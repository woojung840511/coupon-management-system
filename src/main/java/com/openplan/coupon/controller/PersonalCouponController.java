package com.openplan.coupon.controller;

import com.openplan.coupon.config.CommonApiResponses;
import com.openplan.coupon.dto.CouponFilterRequest;
import com.openplan.coupon.dto.PersonalCouponCreateRequest;
import com.openplan.coupon.dto.PersonalCouponResponse;
import com.openplan.coupon.dto.PersonalCouponUseRequest;
import com.openplan.coupon.dto.UserCouponDetailResponse;
import com.openplan.coupon.service.PersonalCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "사용자 쿠폰 사용 API", description = "사용자에게 쿠폰을 발급하고 쿠폰을 사용하는 API")
public class PersonalCouponController {

    private final PersonalCouponService personalCouponService;

    @PostMapping
    @Operation(
        summary = "사용자 쿠폰 발급",
        description = "쿠폰 코드를 사용자에게 발급합니다. 사용자 ID, 관리자 ID, 쿠폰 코드가 필요합니다."
    )
    @ApiResponse(
        responseCode = "201",
        description = "쿠폰 발급 성공",
        content = @Content(schema = @Schema(implementation = PersonalCouponResponse.class))
    )
    @CommonApiResponses
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
    @Operation(
        summary = "쿠폰 사용",
        description = "사용자가 발급받은 쿠폰을 사용합니다. 쿠폰 사용 시 보험 계약 정보가 기록됩니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "쿠폰 사용 성공"
    )
    @CommonApiResponses
    public ResponseEntity<Void> usePersonalCoupon(
        @Parameter(description = "사용자 쿠폰 ID") @PathVariable(value = "personCouponId") String personCouponId,
        @Valid @RequestBody PersonalCouponUseRequest personalCouponUseRequest
    ) {
        personalCouponService.usePersonalCoupon(personCouponId, personalCouponUseRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/{personId}/search")
    @Operation(
        summary = "사용자 쿠폰 목록 조회",
        description = "특정 사용자가 보유한 쿠폰 목록을 조회합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonalCouponResponse.class)))
    )
    @CommonApiResponses
    public ResponseEntity<List<UserCouponDetailResponse>> searchUserCoupons(
        @Parameter(description = "사용자 ID") @PathVariable(value = "personId") String personId,
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
