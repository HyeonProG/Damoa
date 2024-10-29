package com.damoa.repository.interfaces;

import com.damoa.repository.model.Skill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SkillRepository {
    
    // 기술 스택 이름을 id로 조회
    public int findSkillIdByName(@Param("skill") String skill);

    public Skill selectSkillsByName(String name);

    // 기술 모두 가져오기
    public List<Skill> selectAllSkill();

    // Project_skill_tb에 데이터 추가
    public void addProjectSkillData(@Param("skill")int skill, @Param("userId")int userId);
}
