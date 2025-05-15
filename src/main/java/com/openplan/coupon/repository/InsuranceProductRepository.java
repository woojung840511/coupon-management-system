package com.openplan.coupon.repository;

import com.openplan.coupon.entity.InsuranceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceProductRepository extends JpaRepository<InsuranceProduct, String> {
}
