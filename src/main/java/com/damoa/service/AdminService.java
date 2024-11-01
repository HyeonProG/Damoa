package com.damoa.service;

import com.damoa.dto.TossHistoryDTO;
import com.damoa.dto.admin.NoticeDTO;
import com.damoa.dto.user.MonthlyRegisterDTO;
import com.damoa.repository.interfaces.UserRepository;
import com.damoa.repository.model.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.damoa.dto.admin.AdminSignInDTO;
import com.damoa.dto.user.UserSignInDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.interfaces.AdminRepository;
import com.damoa.repository.model.Admin;
import com.damoa.repository.model.User;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final UserRepository userRepository;

    /**
     * 어드민 로그인 확인
     *
     * @param adminSignInDTO
     * @return
     */
    public Admin findAdmin(AdminSignInDTO adminSignInDTO) {
        Admin admin = adminRepository.findByUsername(adminSignInDTO.getUsername());

        if (admin == null) {
            throw new DataDeliveryException("존재하지 않는 아이디 입니다.", HttpStatus.BAD_REQUEST);
        }

        if (admin.getPassword() == null) {
            throw new DataDeliveryException("비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        return admin;

    }

    public List<MonthlyRegisterDTO> getMonthlyRegisterData() {
        return userRepository.getMonthlyRegisterData();
    }

    /**
     * 어드민 회원관리 페이징 처리 서비스
     *
     * @param pageable
     * @return
     */
    public Page<User> getAllUser(Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        List<User> userList = adminRepository.getUserList(pageable.getPageSize(), offset);
        int totalCount = adminRepository.countUser();
        return new PageImpl<>(userList, pageable, totalCount);
    }

    public List<User> getUserList(int limit, int offset) {
        return adminRepository.getUserList(limit, offset);
    }

    public User getUserDetail(int id) {
        return adminRepository.getUserDetail(id);
    }

    // 공지 삭제
    public void deleteNotice(int id) {
        // 1. 유효한 ID인지 확인
        if (id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 ID입니다. ID는 1 이상이어야 합니다.");
        }

        // 2. 공지를 삭제하고 결과를 확인
        int result = adminRepository.deleteNoticeById(id);

        // 3. 삭제가 실패한 경우 예외를 발생시킴
        if (result <= 0) {
            throw new NoSuchElementException("해당 ID를 가진 공지를 찾을 수 없습니다.");
        }
    }

    /**
     * 어드민 공지글 작성
     *
     * @param title
     * @param content
     */
    @Transactional
    public void createNotice(String title, String content) {
        int result = adminRepository.insertNotice(title, content);
        if (result != 1) {
            throw new RuntimeException("데이터 삽입 실패" + result);
        }
    }

    public NoticeDTO findNotice(int id) {
        NoticeDTO dto = adminRepository.findNoticeById(id);
        return dto;
    }

    /**
     * 어드민 공지사항 업데이트
     *
     * @param dto
     */
    @Transactional
    public void updateNotice(int id, NoticeDTO dto) {
        adminRepository.updateNotice(id, dto.getTitle(), dto.getContent());
    }

    public Page<NoticeDTO> getAllNotice(Pageable pageable) {

        int offset = pageable.getPageNumber() * pageable.getPageSize();
        List<NoticeDTO> noticeList = adminRepository.viewAllNotice(offset, pageable.getPageSize());
        int totalCount = adminRepository.countNotice();
        return new PageImpl<>(noticeList, pageable, totalCount);
    }
}
