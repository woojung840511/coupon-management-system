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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponInfoSeq;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType couponType;

    @Column(nullable = false)
    private String couponName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponPublishType couponPublishType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurposeType purposeType;

    private String purposeValue;

    @Enumerated(EnumType.STRING)
    private CouponBadgeType couponBadgeType;

    private String couponImageUrl;

    @Column(nullable = false)
    private Integer pressCount;     // 쿠폰 발행 숫자

    @Column(nullable = false)
    private Integer useCount;       // 쿠폰 사용 숫자

    private Integer limitCount;     // 쿠폰 사용 제한 숫자

    @Column(nullable = false)
    private Boolean isAble;

    @Column(nullable = false)
    private Boolean isDuplicate;

    @Column(nullable = false)
    private LocalDateTime couponStartAt;

    @Column(nullable = false)
    private LocalDateTime couponEndAt;

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
}
