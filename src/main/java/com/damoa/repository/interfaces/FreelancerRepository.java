package com.damoa.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.damoa.repository.model.Freelancer;

@Mapper
public interface FreelancerRepository {

    public int insertFreelancer(Freelancer freelancer); // 프리랜서 등록

}
