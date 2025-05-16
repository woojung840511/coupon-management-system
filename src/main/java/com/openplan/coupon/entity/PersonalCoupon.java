package com.openplan.coupon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder(access = lombok.AccessLevel.PROTECTED)
public class PersonalCoupon {

    @Id
    @Column(nullable = false)
    private String personCouponId; // UUID를 문자열로 저장

    @Column(nullable = false)
    private String personId; // 사용자 UUID

    @Column(nullable = false)
    private String couponCode;

    private LocalDateTime useAt;

    private String insuranceSubscriptionDetailsId; // 보험 계약 정보 UUID

    @Column(columnDefinition = "TEXT")
    private String useData; // JSON 데이터로 저장될 가능성이 있어 TEXT 타입으로 지정

    @PrePersist
    public void generateId() {
        if (personCouponId == null) {
            personCouponId = UUID.randomUUID().toString();
        }
    }

    public static PersonalCoupon create(String personId, String couponCode) {
        return PersonalCoupon.builder()
            .personId(personId)
            .couponCode(couponCode)
            .build();
    }

}
