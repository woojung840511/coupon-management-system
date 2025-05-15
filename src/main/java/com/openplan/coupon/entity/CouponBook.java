package com.openplan.coupon.entity;

import com.openplan.coupon.entity.couponInfo.CouponInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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

}