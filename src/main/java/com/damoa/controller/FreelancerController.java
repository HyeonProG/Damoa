
package com.damoa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.damoa.dto.freelancer.RegisterFreelancerDTO;
import com.damoa.repository.model.Freelancer;
import com.damoa.repository.model.User;
import com.damoa.service.FreelancerService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

        // 사용자 정보 세션에서 가져오기
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phoneNumber", user.getPhoneNumber());

        // 프리랜서 등록 페이지로 이동
        return "freelancer/register_freelancer";
    }

    /**
     * 프리랜서 등록 로직
     * 
     * @param dto
     * @param session
     * @return
     */
    @PostMapping("/register")
    public String registerFreelancerProc(@ModelAttribute RegisterFreelancerDTO dto,
            HttpSession session) {

        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("principal");
        if (user == null) {
            return "redirect:/user/sign-in"; // 로그인 페이지로 리디렉션
        }

        // 세션 유저id 받아오기
        dto.setUserId(user.getId());
        System.out.println("==============================");
        System.out.println("UPLOAED FILE : " + dto.getMFile().getOriginalFilename());
        System.out.println("==============================");

        freelancerService.insertFreelancer(dto);

        return "redirect:/main";
    }

    /**
     * 프리랜서 찾기 리스트 페이지
     * 
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String findFreelancerPage(Model model) {
        return "/freelancer/freelancer_list";
    }

    /**
     * 프리랜서 목록 AJAX 요청을 처리할 엔드포인트
     * 
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/data")
    @ResponseBody
    public Map<String, Object> fetchFreelancerList(@RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {

        // 프리랜서 목록을 페이지별로 조회
        List<Freelancer> freelancers = freelancerService.findAllFreelancers(page, size);
        int totalFreelancers = freelancerService.countAllFreelancers();
        int totalPages = (int) Math.ceil((double) totalFreelancers / size);

        // 응답 데이터
        Map<String, Object> response = new HashMap<>();
        response.put("freelancers", freelancers);
        response.put("totalCount", totalFreelancers);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalPages", totalPages);

        return response; // JSON 형태로 응답
    }
}