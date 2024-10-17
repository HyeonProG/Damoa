package com.damoa.controller;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.damoa.dto.UserSignUpDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 방식 선택 페이지
     * http://localhost:8080/user/sign-up-intro
     * @return signUpIntro.mustache
     */
    @GetMapping("/sign-up-intro")
    public String signUpIntroPage() {
        return "user/signUpIntro";
    }

    /**
     * 프리랜서 회원가입 페이지
     * @param model
     * @return
     */
    @GetMapping("/sign-up/freelancer")
    public String signUpFreelancerPage(Model model) {
        model.addAttribute("userType", "freelancer");
        model.addAttribute("socialType", "local");
        return "user/signUp";
    }

    /**
     * 기업 회원가입 페이지
     * @param model
     * @return
     */
    @GetMapping("/sign-up/company")
    public String signUpCompanyPage(Model model) {
        model.addAttribute("userType", "company");
        model.addAttribute("socialType", "local");
        return "user/signUp";
    }

    /**
     * 이메일 중복 체크
     * @param email
     * @return
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, String>> checkDuplicateEmail(@RequestParam("email") String email) {
        Map<String, String> response = new HashMap<>();

        int result = userService.checkDuplicateEmail(email);

        if (result != 0) {
            System.out.println("중복된 이메일 작동");
            response.put("message", "중복된 이메일은 사용할 수 없습니다.");
            return ResponseEntity.badRequest().body(response);
        } else {
            response.put("message", "사용가능한 이메일 입니다.");
            return ResponseEntity.ok(response);
        }

    }

    /**
     * 회원가입 로직
     * @param dto
     * @return
     */
    @PostMapping("/sign-up")
    public String signUpProc(UserSignUpDTO dto) {
        // 유효성 검사
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new DataDeliveryException("이메일을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            throw new DataDeliveryException("아이디를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new DataDeliveryException("비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        userService.createUser(dto);
        return "redirect:/user/sign-in";
    }
    
    @GetMapping("/sign-in")
    public String signInPage() {
        return "user/signIn";
    }
    

}
