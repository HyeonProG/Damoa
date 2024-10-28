package com.damoa.repository.interfaces;

import com.damoa.dto.TossHistoryDTO;
import com.damoa.dto.review.ReviewUserDTO;
import com.damoa.dto.user.MonthlyRegisterDTO;
import com.damoa.dto.user.PrincipalDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.damoa.repository.model.User;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository {

    public int insertUser(User user); // 사용자 등록

    public int checkDuplicateEmail(@Param("email") String email); // 이메일 중복 체크

    User findById(int id);

    // 리뷰 데이터 유효성 검사를 위한 이름 조회
    Optional<User> findByUserName(String userName);

    public User findByEmail(String email); // 이메일로 유저 확인

    public int findDuplicatePhoneNumber(String phoneNumber); // 휴대폰 번호 중복 확인

    public int insertDeleteUser(User user); // 탈퇴한 사용자 등록

    public int deleteUser(@Param("userId") int userId); // 사용자 삭제

    public int updateUser(User user); // 유저 정보 수정

    List<MonthlyRegisterDTO> getMonthlyRegisterData();  // 월별 회원가입 수 데이터

    List<TossHistoryDTO> findPaymentDetailByUserId(int userId);

    void updateStatus(int id);
    // 사용자 ID로 사용자 정보 조회
    PrincipalDTO findUserById(@Param("id") int id);

    // 프리랜서 목록 조회 (프리랜서 사용자)
    List<User> findAllFreelancers();

    // 기업 목록 조회 (기업 사용자)
    List<User> findAllCompanies();
}
