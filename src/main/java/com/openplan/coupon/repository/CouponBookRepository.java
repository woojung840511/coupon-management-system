package com.openplan.coupon.repository;

import com.openplan.coupon.entity.CouponBook;
import com.openplan.coupon.entity.CouponInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponBookRepository extends JpaRepository<CouponBook, Long> {

    List<CouponBook> findByCouponInfo(CouponInfo couponInfo);

    boolean existsByCouponCode(String randomCode);

    long countByCouponInfo_CouponInfoSeq(Long couponInfoSeq);
}

