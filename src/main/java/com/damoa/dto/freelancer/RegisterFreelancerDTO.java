package com.damoa.dto.freelancer;

import com.damoa.repository.model.Freelancer;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RegisterFreelancerDTO {

    private Integer id; // pk
    private Integer userId; // 프리랜서 id
    private String jobPart; // 직무
    private String workingStyle; // 근무 방식
    private String expectedSalary; // 희망 연봉
    private String salaryType; // 급여 방식
    private String experience; // 프리랜서 경험
    private String career; // 경력
    private String skill; // 기술 스택
    private byte[] portfolio; // 포트폴리오
    private String detail; // 상세 소개
    private String link; // 링크
    private Timestamp createdAt;

    // 프리랜서 Object 반환
    public Freelancer toFreelancer() {
        return Freelancer.builder()
                .userId(this.userId)
                .jobPart(this.jobPart)
                .workingStyle(this.workingStyle)
                .expectedSalary(this.expectedSalary)
                .salaryType(this.salaryType)
                .experience(this.experience)
                .career(this.career)
                .skill(this.skill)
                .portfolio(this.portfolio)
                .detail(this.detail)
                .link(this.link)
                .createdAt(this.createdAt)
                .build();
    }

}
