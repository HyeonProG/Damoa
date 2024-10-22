package com.damoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.damoa.dto.admin.AdminSignInDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.model.Admin;
import com.damoa.repository.model.User;
import com.damoa.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private final AdminService adminService;

    /**
     * 관리자 메인 페이지
     * 
     * @return
     */
    @GetMapping("/main")
    public String mainPage(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        Admin admin = session != null ? (Admin) session.getAttribute("admin") : null;
//
//        // 로그인을 하지 않았을 경우 로그인 페이지로 리다이렉트
//        if (admin == null) {
//            return "redirect:/admin/sign-in";
//        }

        return "/admin/admin_main";
    }

    /**
     * 관리자 로그인 페이지
     * 
     * @return
     */
    @GetMapping("/sign-in")
    public String adminSignInPage() {
        return "admin/sign_in";
    }

    /**
     * 관리자 로그인
     * 
     * @param username
     * @param password
     * @return
     */
//    @PostMapping("/sign-in")
//    public String adminSignInProc(AdminSignInDTO adminSignInDTO, HttpServletRequest request) {
//        try {
//            Admin admin = adminService.findAdmin(adminSignInDTO);
//            // 세션 생성
//            HttpSession session = request.getSession(true);
//            session.setAttribute("admin", admin);
//            return "redirect:/admin/main";
//        } catch (Exception e) {
//            if (adminSignInDTO.getUsername() == null || adminSignInDTO.getUsername().isEmpty()) {
//                throw new DataDeliveryException("아이디를 입력하세요.", HttpStatus.BAD_REQUEST);
//            }
//            if (adminSignInDTO.getPassword() == null || adminSignInDTO.getPassword().isEmpty()) {
//                throw new DataDeliveryException("비밀번호를 입력하세요.", HttpStatus.BAD_REQUEST);
//            }
//            e.printStackTrace();
//            return "/admin/sign_in";
//        }
//    }

    @GetMapping("/management")
    public String ListPage(Model model){
        List<User> userList = adminService.getAllUser();
        model.addAttribute("userList", userList);
        return "admin/admin_user_list";
    }

    @GetMapping("/{currentPageNum}")
    public String UserListPage(@PathVariable(required = false) int currentPageNum, Model model){

        List<User> allUser = adminService.getAllUser();
        int totalUser = allUser.size();
        int limit = 10;
        int totalPages = totalUser/limit;
        int offset;

        if(currentPageNum == 0 || currentPageNum == 1){
            currentPageNum = 1;
            offset = 0;
        } else {
            offset = currentPageNum*limit+1;
        }

        List<User> userList = adminService.getUserList(limit,offset);

        model.addAttribute("userList",userList);
        model.addAttribute("totalUser",totalUser);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("currentPageNum",currentPageNum);

        return "/admin/admin_notice_list";
    }



}
