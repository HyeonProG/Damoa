
package com.damoa.service;

import com.damoa.repository.interfaces.FreelancerRepository;
import com.damoa.repository.model.BusinessType;
import com.damoa.repository.model.JobTitle;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreelancerService {

    @Autowired
    private final FreelancerRepository freelancerRepository;

    //직무 목록 가져오기
    public List<JobTitle> jobTitles() {
        return freelancerRepository.jobTitles();
    }

    // 사업자 유형
    public List<BusinessType> businessTypes() {
        return freelancerRepository.businessTypes();
    }

}
