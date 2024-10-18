package com.damoa.controller;

import com.damoa.dto.BankAuthDTO;
import com.damoa.service.AccountService;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController {

    // 관리자 메인 페이지
    @GetMapping("/main")
    public String mainPage(){
        return "admin/admin_main";
    }

    @GetMapping("/faq")
    public String faq(){
        return "admin/admin_faq";
    }


}
