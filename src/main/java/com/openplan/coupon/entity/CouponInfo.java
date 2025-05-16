package com.openplan.coupon.entity;

import com.openplan.coupon.enums.CouponBadgeType;
import com.openplan.coupon.enums.CouponPublishType;
import com.openplan.coupon.enums.CouponType;
import com.openplan.coupon.enums.PurposeType;
import com.openplan.coupon.enums.TargetType;
import com.openplan.coupon.exception.ResourceNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponInfo {

    /**
     * 쿠폰북 발행 방식 결정 요소 - 코드 발급 방식 : couponPublishType - UNI("고정 코드") : 쿠폰북 생성 시 발급된 쿠폰코드가 고정됨. 파라미터로 받아야함<- 쿠폰북CreateRequest(String code) 필요 - POLY("임의 코드") : 쿠폰북 생성 시 발급된 쿠폰코드가 임의로 발급됨 - 쿠폰북 발급 개수 :
     * pressCount
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponInfoSeq;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType couponType;      // 1회(limitCount 1), 다회(limitCount 무시), 제한(limitCount N) ?

    @Column(nullable = false)
    private String couponName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;                      // B2B, B2C, ALL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponPublishType couponPublishType;        // UNI("고정 코드"), POLY("임의 코드") {{ 쿠폰북 생성에 관여 }}

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurposeType purposeType;                    // RATE("할인율"), AMOUNT("차감 비용")

    private String purposeValue;

    @Enumerated(EnumType.STRING)
    private CouponBadgeType couponBadgeType;

    private String couponImageUrl;

    @Column(nullable = false)
    private Integer pressCount;                         // 쿠폰 발행 숫자 {{ 쿠폰북 생성에 관여 }}

    @Column(nullable = false)
    private Integer useCount;                           // 쿠폰 사용 숫자 -> 로그성

    private Integer limitCount;                         // 쿠폰 사용 제한 숫자

    @Column(nullable = false)
    private Boolean isAble;                             // 쿠폰 사용 가능 여부
    // 해석 : 쿠폰의 전체적인 활성화/비활성화 제어 (관리자가 중단하고 싶을 때 사용)

    @Column(nullable = false)
    private Boolean isDuplicate;                        // 중복 사용 가능 여부
    // 해석 : 사용자가 같은 종류 (기준: couponInfoSeq) 쿠폰을 여러 번 사용할 수 있는지 여부

    @Column(nullable = false)
    private LocalDateTime couponStartAt;                // 쿠폰 적용 시작 일시

    @Column(nullable = false)
    private LocalDateTime couponEndAt;                  // 쿠폰 적용 종료 일시

    @OneToMany(
        mappedBy = "couponInfo",
        cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CouponCondition> conditions = new ArrayList<>();

    // 양방향 관계 편의 메서드
    public void addCondition(CouponCondition condition) {
        conditions.add(condition);
        condition.setCouponInfo(this);
    }

    public void removeCondition(CouponCondition condition) {
        conditions.remove(condition);
        condition.setCouponInfo(null);
    }

    public CouponCondition getCouponCondition(Long conditionId) {
        return conditions.stream()
            .filter(condition -> condition.getCouponConditionSeq().equals(conditionId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("CouponCondition", "id", conditionId));
    }

    public boolean isCouponCodePublishable() {
        boolean hasRequiredFields =
            couponInfoSeq != null &&
                couponType != null &&
                couponName != null &&
                targetType != null &&
                couponPublishType != null &&
                purposeType != null &&
                purposeValue != null &&
                pressCount != null &&
                useCount != null &&
                isAble != null &&
                isDuplicate != null &&
                couponStartAt != null &&
                couponEndAt != null;

        if (couponType == CouponType.LIMIT && limitCount == null) {
            return false;
        }

        // 추가 비즈니스 규칙 검증
        boolean isValidEndDate = couponEndAt.isAfter(LocalDateTime.now());
        boolean isValidPressCount = pressCount > 0;
        boolean isValidDateRange = couponEndAt.isAfter(couponStartAt);

        return isAble && hasRequiredFields && isValidEndDate && isValidPressCount && isValidDateRange;
    }
}
