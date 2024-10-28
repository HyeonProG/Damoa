package com.damoa.controller;

import com.damoa.dto.BankAuthDTO;
import com.damoa.repository.model.Freelancer;
import com.damoa.repository.model.Project;
import com.damoa.repository.model.User;
import com.damoa.repository.model.Skill;
import com.damoa.service.AccountService;
import com.damoa.service.FreelancerService;
import com.damoa.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FreelancerService freelancerService;

    /**
     * 메인 페이지 이동
     * @param model
     * @return
     */
    @GetMapping("/main")
    public String mainPage(Model model, HttpSession session){
        User user = (User) session.getAttribute("principal");

        List<Project> projectList = projectService.getProjectForPaging(3,0);
        List<Freelancer> freelancerList = freelancerService.findAllFreelancers(1, 3);
        model.addAttribute("projectList",projectList);
        model.addAttribute("isLogin",user);
        if (user != null) {
            model.addAttribute("isFreelancer", user.getUserType().equals("freelancer"));
            model.addAttribute("isCompany", user.getUserType().equals("company"));
        }
        model.addAttribute("freelancerList", freelancerList);
        return "user/index";
    }

    /**
     * 검색 화면
     * @param keyword
     * @param model
     * @return
     */
    @PostMapping("/search")
    public String searchPage(@ModelAttribute("keyword") String keyword, Model model){
        System.out.println("~~~~~~~~"+keyword);

        List<Project> projectList = projectService.findByProjectName(keyword);

        model.addAttribute("projectList",projectList);
        return "user/search";
    }

    // 계좌 인증 페이지 입장
    @GetMapping("/account-list")
    public String registerAccountPage(){
        return "user/register_account";
    }

    // 계좌 인증 페이지 인증 처리
    @PostMapping("/account-request")
    public String registerAccountProc(@ModelAttribute BankAuthDTO reqDto){
        System.out.println(reqDto+"인증 들어옴");
        accountService.addAccountReq(reqDto);
        return "user/success_account";
    }

    // 개인사업자 객체
    @Data
    @ToString
    public class Individual {
        private String name;
        private String email;
        private String phone;
    }
    
    // 계좌 정보
    @Data
    @ToString
    public class Account{
        private String bankCode;
        private String accountNumber ;
        private String holderName ;
    }
}
