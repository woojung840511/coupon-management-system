package com.openplan.coupon.entity;

import com.openplan.coupon.enums.LogType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class CouponLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponLogSeq;

    @Column(nullable = false)
    private String couponCode;

    @Column(nullable = false)
    private String personId; // 사용자 UUID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogType logType;

    @Column(columnDefinition = "TEXT")
    private String logDesc;

}
