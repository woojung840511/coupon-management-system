package com.openplan.coupon.entity;

import com.openplan.coupon.enums.ConditionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class CouponCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponConditionSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_info_seq", nullable = false)
    private CouponInfo couponInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionType conditionType;

    @Column(nullable = false)
    private String mainValue;

    private String subValue;

    private String conditionDesc;
}
