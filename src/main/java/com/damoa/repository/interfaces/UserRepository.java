package com.damoa.repository.interfaces;

import com.damoa.dto.user.MonthlyRegisterDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.damoa.repository.model.User;

import java.util.List;

@Mapper
public interface UserRepository {

    public int insertUser(User user); // 사용자 등록

    public int checkDuplicateEmail(@Param("email")String email); // 이메일 중복 체크

    User findById(int id);

    public User findByEmail(String email); // 이메일로 유저 확인

    public int findDuplicatePhoneNumber(String phoneNumber); // 휴대폰 번호 중복 확인

    public int insertDeleteUser(User user); // 탈퇴한 사용자 등록

    public int deleteUser(@Param("userId") int userId); // 사용자 삭제

    public int updateUser(User user); // 유저 정보 수정

    List<MonthlyRegisterDTO> getMonthlyRegisterData();  // 월별 회원가입 수 데이터

}
