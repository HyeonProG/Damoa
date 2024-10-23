package com.damoa.service;

import com.damoa.dto.ProjectSaveDTO;
import com.damoa.dto.user.ProjectWaitDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.interfaces.ProjectRepository;
import com.damoa.repository.model.Project;
import com.damoa.repository.model.ProjectWait;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

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
            ,dto.getProjectName(), dto.getProjectStart(),dto.getExpectedPeriod(),dto.getPeriod(),dto.getSalaryType(),dto.getWorkingStyle(),dto.getMeeting(),dto.getAddress()
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

    // 모든 프로젝트 가져오기
    public List<Project> getAllProject() {
        return projectRepository.getAllProject();
    }

    // 프로젝트 가져오기 (*페이징)
    public List<Project> getProjectForPaging(int limit, int offset) {
        return projectRepository.getProjectForPaging(limit,offset);
    }

    // id로 프로젝트 찾기
    public Project findProjectById(int projectId) {
        return projectRepository.getProjectById(projectId);
    }

    // 프로젝트 대기열 추가
    public void makeNewWait(ProjectWait reqDTO) {
        projectRepository.addNewWait(reqDTO);
        return;
    }
}
