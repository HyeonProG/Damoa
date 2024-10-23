package com.damoa.service;

import com.damoa.dto.user.MonthlyRegisterDTO;
import com.damoa.repository.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    @Autowired
    private final AdminRepository adminRepository;

    @Autowired
    private final UserRepository userRepository;

    /**
     * 어드민 로그인 확인
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
}
