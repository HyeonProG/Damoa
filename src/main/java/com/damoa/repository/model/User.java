package com.damoa.repository.model;

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
public class User {
    private Integer id;
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private Timestamp createdAt;
    private String userType; // 프리랜서인지 기업인지 나누는 변수
    private String socialType; // 소셜로그인, 로컬로 나누는 변수
}
