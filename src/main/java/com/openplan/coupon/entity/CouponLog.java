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
@Builder(access = lombok.AccessLevel.PROTECTED)
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


    public static CouponLog ofPublish(String couponCode, String adminId, String logDesc) {
        return CouponLog.builder()
            .couponCode(couponCode)
            .personId(adminId) // CouponBook 발행자 ID
            .logType(LogType.PUBLISH)
            .logDesc(logDesc)
            .build();
    }

    public static CouponLog ofTransfer(String couponCode, String adminId, String logDesc) {
        return CouponLog.builder()
            .couponCode(couponCode)
            .personId(adminId) // PersonalCoupon 전송자 ID
            .logType(LogType.TRANS)
            .logDesc(logDesc)
            .build();
    }

    public static CouponLog ofUsage(String couponCode, String userId, String logDesc) {
        return CouponLog.builder()
            .couponCode(couponCode)
            .personId(userId) // 사용자 ID
            .logType(LogType.USE)
            .logDesc(logDesc)
            .build();
    }

}
