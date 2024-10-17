package com.damoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.damoa.dto.UserSignUpDTO;
import com.damoa.repository.interfaces.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 서비스 기능 트랜잭션 처리
     * 
     * @param dto
     */
    @Transactional
    public void createUser(UserSignUpDTO dto) {
        System.out.println("createUser 동작");
        int result = 0;

        try {
            String hashPwd = passwordEncoder.encode(dto.getPassword());
            dto.setPassword(hashPwd);

            result = userRepository.insertUser(dto.toUser());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 이메일 중복 체크
     * 
     * @param email
     * @return
     */
    @Transactional
    public int checkDuplicateEmail(String email) {
        int result = 0;
        result = userRepository.checkDuplicateEmail(email);
        if (result != 0) {
            return 1;
        }
        return 0;
    }

}
