package com.damoa.service;

import com.damoa.repository.interfaces.SkillRepository;
import com.damoa.repository.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public Skill findSkillsByname(String name) {
        return skillRepository.getSkillsByName(name);
    }

    public List<Skill> getAllSkill(){
        return skillRepository.selectAllSkill();
    }

}
