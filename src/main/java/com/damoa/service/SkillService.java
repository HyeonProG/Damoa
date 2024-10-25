package com.damoa.service;

import com.damoa.repository.interfaces.SkillRepository;
import com.damoa.repository.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;
    public List<Skill> getAllSkill(){
        return skillRepository.selectAllSkill();
    }

    /**
     * 유저가 입력한 정보를 기반으로 skill List 반환
     * @param strList
     * @return
     */
    public List<Skill> findSkillListByName(List<String> strList) {
        List<Skill> skillList = new ArrayList<>();
        for(int i=0; i<strList.size(); i++){
            Skill skill = skillRepository.selectSkillsByName(strList.get(i));
            skillList.add(skill);
        };
        return skillList;
    }

    /**
     * skill List를 통해 prokect_skill_tb를 생성
     * @param skillList
     */
    public void addProjectSkillData(List<Skill> skillList, int userId) {
        int size = skillList.size();
        for(int i=0; i<size; i++){
            skillRepository.addProjectSkillData(skillList.get(i).getId(),userId);
        }
    }
}
