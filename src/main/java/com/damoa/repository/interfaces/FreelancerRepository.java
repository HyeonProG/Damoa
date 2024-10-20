package com.damoa.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.damoa.repository.model.Freelancer;

@Mapper
public interface FreelancerRepository {

    public int insertFreelancer(Freelancer freelancer); // 프리랜서 등록

    public List<Freelancer> findAllFreelancers(@Param("offset")int offset, @Param("size") int size); // 전체 프리랜서 조회

    public int countAllFreelancers(); // 전체 프리랜서 수 조회

}
