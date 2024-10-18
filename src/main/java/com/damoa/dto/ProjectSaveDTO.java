package com.damoa.dto;

import com.damoa.repository.model.Project;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


@Data
public class ProjectSaveDTO {
    private int id;
    private int userId;
    private String job;
    private String skill;
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
    private byte[] progress; // Blob 타입을 byte 배열로
    private byte[] mainTasks; // Blob 타입을 byte 배열로
    private byte[] detailTask; // Blob 타입을 byte 배열로
    private byte[] delivered; // Blob 타입을 byte 배열로
    private MultipartFile files; // Blob 타입을 byte 배열로
    private String company;
    private String managerName;
    private String contact;
    private String email;
    private int agree;



}
