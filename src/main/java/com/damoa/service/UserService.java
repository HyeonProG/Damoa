package com.damoa.service;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.damoa.dto.user.PrincipalDTO;
import com.damoa.dto.user.UserSignInDTO;
import com.damoa.dto.user.UserSignUpDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.interfaces.UserRepository;
import com.damoa.repository.model.User;

import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Value("${sms.api-key}")
    private String apiKey;

    @Value("${sms.api-secret}")
    private String apiSecret;

    @Value("${sms.from-phone-number}")
    private String fromPhoneNumber;

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

    /**
     * 전화번호 인증 api
     * @param phoneNumber
     * @param cerNum
     */
    public void certifiedPhoneNumber(String phoneNumber, String cerNum) {

        Message coolsms = new Message(apiKey, apiSecret);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber); // 수신전화번호
        params.put("from", fromPhoneNumber); // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "다모아 휴대폰 인증 : 인증번호는" + "[" + cerNum + "]" + "입니다.");
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

    }

    /**
     * 휴대폰 번호 체크
     * @param phoneNumber
     * @return
     */
    public boolean findDuplicatePhoneNumber(String phoneNumber) {
        int result = userRepository.findDuplicatePhoneNumber(phoneNumber);
        if (result!= 0) {
            return true;
        }
        return false;
    }

    /**
     * 이메일로 유저 찾기
     * @param email
     * @return
     */
    public PrincipalDTO findByUserEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            PrincipalDTO principalDTO = PrincipalDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .socialType(user.getSocialType())
                    .userType(user.getUserType())
                    .build();
            return principalDTO;
        }
        return null;
    }

    /**
     * 입력한 이메일, 비밀번호가 DB와 동일한지 확인
     * @param userSignInDTO
     * @return
     */
    public User findUser(UserSignInDTO userSignInDTO) {
        User user = userRepository.findByEmail(userSignInDTO.getEmail());

       if (user == null) {
            throw new DataDeliveryException("존재하지 않는 아이디 입니다.", HttpStatus.BAD_REQUEST);
        }
        
        boolean isPwdMatched = passwordEncoder.matches(userSignInDTO.getPassword(), user.getPassword());
        if (!isPwdMatched) {
            throw new DataDeliveryException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        return user;

    }

    @Transactional
    public void deleteUserAccount(User user) {
        userRepository.insertDeleteUser(user);

        userRepository.deleteUser(user.getId());
    }


}
