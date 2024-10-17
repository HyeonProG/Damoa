package com.damoa.service;

import com.damoa.dto.ProjectSaveDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.interfaces.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public void createProject(ProjectSaveDTO dto){
        int result = 0;

        try {
            result = projectRepository.insertProject(dto.getUserId(),dto.getUserId(),dto.getJob(),dto.getSkill(),dto.getMinYears(),dto.getMaxYears()
            ,dto.getProjectStart(),dto.getExpectedPeriod(),dto.getPeriod(),dto.getSalaryType(),dto.getWorkingStyle(),dto.getMeeting(),dto.getAddress()
            ,dto.getWorktime(),dto.getWorkAdjust(),dto.getVacation(),dto.getProgress(),dto.getMainTasks(),dto.getDetailTask(),dto.getDetailTask()
            ,dto.getDelivered(), dto.getFiles().getBytes(),dto.getCompany(),dto.getManagerName(),dto.getContact(),dto.getEmail(),dto.getAgree());
        } catch (DataDeliveryException e){
            throw new DataDeliveryException("잘못된 요청입니다", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(result == 0){
            throw new DataDeliveryException("정상 처리 되지 않았습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
