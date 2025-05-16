package com.openplan.coupon.service;

import com.openplan.coupon.entity.InsuranceContract;
import com.openplan.coupon.exception.ResourceNotFoundException;
import com.openplan.coupon.repository.InsuranceContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InsuranceService {

    private final InsuranceContractRepository insuranceContractRepository;

    public InsuranceContract getInsuranceContractEntity(String contractId) {
        return insuranceContractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("InsuranceContract", "contractId", contractId));
    }

}
