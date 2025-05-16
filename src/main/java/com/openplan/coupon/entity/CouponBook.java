package com.openplan.coupon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CouponBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponBookSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_info_seq", nullable = false)
    private CouponInfo couponInfo;

    @Column(nullable = false, unique = true)
    private String couponCode;

    @Column(nullable = false)
    private Boolean isUsed;

    private LocalDateTime expireAt;

    public CouponBook(CouponInfo couponInfo, String couponCode) {

        if (! couponInfo.isCouponCodePublishable()) {
            throw new IllegalArgumentException("쿠폰코드 발급이 불가능한 쿠폰정보입니다.");
        }

        this.couponInfo = couponInfo;
        this.couponCode = couponCode;
        this.isUsed = false;
        this.expireAt = couponInfo.getCouponEndAt();
    }
}