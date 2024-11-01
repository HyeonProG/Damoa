package com.damoa.repository.interfaces;

import com.damoa.dto.admin.NoticeDTO;
import com.damoa.repository.model.User;
import org.apache.ibatis.annotations.Mapper;

import com.damoa.repository.model.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminRepository {

    public Admin findByUsername(String username);

    public List<User> getUserList(@Param("limit") int limit, @Param("offset") int offset);

    public List<User> getAllUser();

    public User getUserDetail(int id);

    // 어드민 유저 카운트
    int countUser();

    // 어드민 공지 수정
    int updateNotice(@Param("id") int id, @Param("title") String title, @Param("content") String content);

    // 어드민 공지 삭제
    int deleteNoticeById(int id);

    // 어드민 공지 추가
    int insertNotice(@Param("title") String title, @Param("content") String content);

    // 어드민 공지 조회
    NoticeDTO findNoticeById(int id);

    // 어드민 공지 카운트
    int countNotice();

    // 어드민 공지 리스트 뽑기
    List<NoticeDTO> viewAllNotice(@Param("offset") int offset, @Param("pageSize") int pageSize);
}
