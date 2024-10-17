package com.damoa.repository.interfaces;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.damoa.repository.model.User;

@Mapper
public interface UserRepository {

    public int insertUser(User user); // 사용자 등록

    public int checkDuplicateEmail(@Param("email")String email); // 이메일 중복 체크

    public User findByEmail(String email); // 이메일로 유저 확인

    public int findDuplicatePhoneNumber(String phoneNumber); // 휴대폰 번호 중복 확인

}
