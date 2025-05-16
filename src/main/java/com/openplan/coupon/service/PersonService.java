package com.openplan.coupon.service;

import com.openplan.coupon.entity.Person;
import com.openplan.coupon.exception.ResourceNotFoundException;
import com.openplan.coupon.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonService {

    private final PersonRepository personRepository;

    public Person getPersonEntity(String personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person", "personId", personId));
    }
}
