package com.openplan.coupon.repository;

import com.openplan.coupon.entity.InsuranceContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceContractRepository extends JpaRepository<InsuranceContract, String> {
}