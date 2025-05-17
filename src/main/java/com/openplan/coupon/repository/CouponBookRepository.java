package com.openplan.coupon.repository;

import com.openplan.coupon.entity.CouponBook;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponBookRepository extends JpaRepository<CouponBook, Long> {

    boolean existsByCouponCode(String randomCode);

    Optional<CouponBook> findByCouponCode(String couponCode);

    long countByCouponInfo_CouponInfoSeq(Long couponInfoSeq);
    /*
    SELECT COUNT(*)
    FROM coupon_book cb
    JOIN coupon_info ci ON cb.coupon_info_seq = ci.coupon_info_seq
    WHERE ci.coupon_info_seq = ?
     */
}

