package com.damoa.repository.interfaces;

import com.damoa.repository.model.BusinessType;
import com.damoa.repository.model.JobTitle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FreelancerRepository {
    List<JobTitle> jobTitles(); // 직무 목록
    List<BusinessType> businessTypes(); // 사업자 유형 목록
}
