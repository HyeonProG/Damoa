package com.damoa.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {

    @Autowired
    public HttpSession session;

    @GetMapping("/notice")
    public String noticePage(){
        return "/user/user_notice_list";
    }

}
