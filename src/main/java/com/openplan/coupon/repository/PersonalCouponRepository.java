package com.openplan.coupon.repository;

import com.openplan.coupon.entity.PersonalCoupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalCouponRepository extends JpaRepository<PersonalCoupon, String> {

    int countByCouponCode(String couponCode);

    @Query("""
    select COUNT(p) from PersonalCoupon p
    join CouponBook cb on p.couponCode = cb.couponCode
    join CouponInfo ci on cb.couponInfo.couponInfoSeq = ci.couponInfoSeq
    where p.personId = :personId and ci.couponInfoSeq = :couponInfoSeq
    """)
    int countByPersonIdAndCouponInfoSeq(
        @Param("personId") String personId,
        @Param("couponInfoSeq") Long couponInfoSeq
    );

    List<PersonalCoupon> findByPersonId(String personId);
}

