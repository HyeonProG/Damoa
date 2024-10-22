package com.damoa.repository.interfaces;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SkillRepository {
    
    // 기술 스택 이름을 id로 조회
    public int findSkillIdByName(@Param("skill") String skill);

}
