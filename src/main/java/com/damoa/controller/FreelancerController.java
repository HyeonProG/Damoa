package com.damoa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.damoa.dto.freelancer.RegisterFreelancerDTO;
import com.damoa.repository.model.Freelancer;
import com.damoa.repository.model.User;
import com.damoa.service.FreelancerService;
import java.util.List;
import java.util.ArrayList;
import edu.emory.mathcs.backport.java.util.Arrays;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/freelancer")
public class FreelancerController {

    private final FreelancerService freelancerService;

    @GetMapping("/register")
    public String registerFreelancerPage(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("principal");

        // 사용자가 로그인하지 않았을 경우 로그인 페이지로 리디렉션
        if (user == null) {
            return "redirect:/user/sign-in";
        }

        // 사용자가 프리랜서인지 확인
        if (!"freelancer".equals(user.getUserType())) {
            throw new IllegalArgumentException("프리랜서만 접근할 수 있습니다.");
        }

        // 직무 카테고리 리스트
        // TODO DB에 리스트를 저장하고 가져오도록 설계해야함
        List<String> jobCategories = List.of("프론트엔드", "백엔드", "풀스택", "모바일", "데브옵스", "디자인");
        // 근무 방식 리스트
        List<String> workingStyleCategories = List.of("재택 근무", "출퇴근", "파트타임", "풀타임");
        // 급여 방식 리스트
        List<String> salaryTypeCategories = List.of("시급", "월급", "프로젝트");
        // 경력 직무 리스트
        List<String> careerCategories = List.of("프론트엔드", "백엔드", "풀스택", "모바일", "데브옵스", "디자인");
        // 스킬 스택 리스트
        List<String> skillCategories = List.of("Java", "JavaScript", "Python", "MySQL", "Android");

        // 카테고리를 모델에 추가
        model.addAttribute("jobCategories", jobCategories);
        model.addAttribute("workingStyleCategories", workingStyleCategories);
        model.addAttribute("salaryTypeCategories", salaryTypeCategories);

        model.addAttribute("careerCategories", careerCategories);
        model.addAttribute("skillCategories", skillCategories);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phoneNumber", user.getPhoneNumber());

        // 프리랜서 등록 페이지로 이동
        return "freelancer/register_freelancer";
    }

    @PostMapping("/register")
    public String createFreelancer(RegisterFreelancerDTO dto) {
        freelancerService.insertFreelancer(dto);
        return "redirect:/user/main";
    }

}
