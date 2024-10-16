package com.damoa.controller;

import com.damoa.dto.BankAuthDTO;
import com.damoa.service.AccountService;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


    @GetMapping("/main")
    public String mainPage(){
        return "index";
    }

    // 계좌 인증 페이지 입장
    @GetMapping("/account-list")
    public String registerAccountPage(){
        return "register_account";
    }

    // 계좌 인증 페이지 인증 처리
    @PostMapping("/account-request")
    public String registerAccountProc(@ModelAttribute BankAuthDTO reqDto){
        accountService.addAccountReq(reqDto);
        return null;
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
