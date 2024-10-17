package com.damoa.controller;

import com.damoa.repository.model.BusinessType;
import com.damoa.repository.model.JobTitle;
import com.damoa.service.FreelancerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FreelancerController {

    private final FreelancerService freelancerService;

    /**
     * 프리랜서 등록 페이지 요청
     * @param model
     * @return
     */
    @GetMapping("/freelancer/register")
    public String showFreelancerRegisterForm(Model model) {
        // 직무 목록 가져오기
        List<JobTitle> jobTitles = freelancerService.jobTitles();
        // 사업자 유형 목록 가져오기
        List<BusinessType> businessTypes = freelancerService.businessTypes();
        // 모델에 추가 후 뷰로 전달
        model.addAttribute("jobTitles", jobTitles);
        model.addAttribute("businessTypes", businessTypes);
        return "freelancer/register_freelancer";
    }

    /**
     * 프리랜서 등록 기능
     * @param name
     * @param email
     * @param jobTitlesCsv
     * @param contact
     * @param businessType
     * @return
     */
    @PostMapping("/freelancer/register")
    public String registerFreelancer(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("job_titles") String jobTitlesCsv,  // CSV 형태로 전달됨
            @RequestParam("contact") String contact,
            @RequestParam("business_type") String businessType) {

        List<String> jobTitles = Arrays.asList(jobTitlesCsv.split(","));
        // 직무 처리 로직 추가

        return "redirect:/freelancer/success";
    }
}
