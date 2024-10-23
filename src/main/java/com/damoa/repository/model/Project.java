package com.damoa.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {

    private int id;
    private int userId;
    private String job;
    private String skill;
    private String projectName;
    private int minYears;
    private int maxYears;
    private Date projectStart;
    private String expectedPeriod;
    private int period;
    private String salaryType;
    private String workingStyle;
    private String meeting;
    private String address;
    private String worktime;
    private String workAdjust;
    private String vacation;
    private String progress; // Blob 타입을 byte 배열로
    private String mainTasks; // Blob 타입을 byte 배열로
    private String detailTask; // Blob 타입을 byte 배열로
    private String delivered; // Blob 타입을 byte 배열로
    private byte[] files; // Blob 타입을 byte 배열로
    private String company;
    private String managerName;
    private String contact;
    private String email;
    private Timestamp createdAt;
    private int majorStatus;
    private int minorStatus;

}
