package com.damoa.repository.interfaces;

import com.damoa.repository.model.Project;
import com.damoa.repository.model.ProjectWait;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Mapper
public interface ProjectRepository {
    public int updateById(Project project);

    public int deleteById(Project project);

    int insertProject(
            @Param("id") int id,
            @Param("userId") int userId,
            @Param("job") String job,
            @Param("minYears") int minYears,
            @Param("maxYears") int maxYears,
            @Param("projectName") String projectname,
            @Param("projectStart") LocalDate projectStart,
            @Param("expectedPeriod") String expectedPeriod,
            @Param("period") int period,
            @Param("salaryType") String salaryType,
            @Param("workingStyle") String workingStyle,
            @Param("meeting") String meeting,
            @Param("address") String address,
            @Param("worktime") String worktime,
            @Param("workAdjust") String workAdjust,
            @Param("vacation") String vacation,
            @Param("progress") byte[] progress,
            @Param("mainTasks") byte[] mainTasks,
            @Param("detailTask") byte[] detailTask,
            @Param("delivered") byte[] delivered,
            @Param("files") byte[] files,
            byte[] dtoFiles, @Param("company") String company,
            @Param("managerName") String managerName,
            @Param("contact") String contact,
            @Param("email") String email,
            @Param("agree") int agree
    );

    // 모든 프로젝트 가져오기
    List<Project> getAllProject();

    // 프로젝트 가져오기 (페이징
    List<Project> getProjectForPaging(@Param("limit")int limit, @Param("offset")int offset);

    // id로 프로젝트 가져오기
    Project getProjectById(int projectId);

    // 프로젝트 대기열 생성
    void addNewWait(ProjectWait reqDTO);
}
