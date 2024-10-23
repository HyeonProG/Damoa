package com.damoa.dto.user;

import com.damoa.repository.model.ProjectWait;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWaitDTO {
    private int projectId;
    private int userId;
    private String introduce;
    private long minimumWage;
    private long maximumWage;
    private LocalDate startDate;
    private String skill;
    private MultipartFile file;
    private String detail;

    public static ProjectWait toProWait(ProjectWaitDTO projectWaitDTO){
        return ProjectWait.builder()
                .userId(projectWaitDTO.userId)
                .projectId(projectWaitDTO.getProjectId())
                .introduce(projectWaitDTO.getIntroduce())
                .minimumWage(projectWaitDTO.minimumWage)
                .maximumWage(projectWaitDTO.maximumWage)
                .startDate(projectWaitDTO.startDate)
                .detail(projectWaitDTO.detail)
                .build();
    }
}


