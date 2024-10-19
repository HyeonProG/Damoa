package com.damoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.damoa.dto.freelancer.RegisterFreelancerDTO;
import com.damoa.repository.interfaces.FreelancerRepository;
import com.damoa.repository.model.Freelancer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreelancerService {

    @Autowired
    private final FreelancerRepository freelancerRepository;

    /**
     * 프리랜서 등록
     * 
     * @param dto
     * @return
     */
    @Transactional
    public void insertFreelancer(RegisterFreelancerDTO dto) {
        int result = 0;
        try {
            result = freelancerRepository.insertFreelancer(dto.toFreelancer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
