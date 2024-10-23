package com.damoa.controller;

import com.damoa.dto.user.MonthlyRegisterDTO;
import com.damoa.dto.user.MonthlyVisitorDTO;
import com.damoa.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import com.damoa.dto.admin.AdminSignInDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.model.Admin;
import com.damoa.repository.model.User;
import com.damoa.service.AdminService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private final AdminService adminService;

    @Autowired
    private final VisitorService visitorService;

    /**
     * 관리자 메인 페이지
     * 
     * @return
     */
    @GetMapping("/main")
    public String mainPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Admin admin = session != null ? (Admin) session.getAttribute("admin") : null;
        
//        // 로그인을 하지 않았을 경우 로그인 페이지로 리다이렉트
//        if (admin == null) {
//            return "redirect:/admin/sign-in";
//        }

        // 방문자의 IP 주소를 가져와서 방문자 기록
        String userIp = request.getRemoteAddr(); // 클라이언트의 IP 주소를 얻음
        visitorService.recordVisitor(userIp); // 방문자 기록

        return "/admin/main";
    }

//    /**
//     * 관리자 로그인 페이지
//     *
//     * @return
//     */
//    @GetMapping("/sign-in")
//    public String adminSignInPage() {
//        return "admin/sign_in";
//    }
//
//    /**
//     * 관리자 로그인
//     *
//     * @param username
//     * @param password
//     * @return
//     */
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
///    }

    @GetMapping("/faq")
    public String faq(){
        return "admin/admin_faq";
    }

    /**
     * 월별 회원 수 데이터 반환
     * @return
     */
    @GetMapping("/monthly-registers")
    public ResponseEntity<List<MonthlyRegisterDTO>> getMonthlyRegisterData() {
        List<MonthlyRegisterDTO> registerDataList = adminService.getMonthlyRegisterData();
        return new ResponseEntity<>(registerDataList, HttpStatus.OK); // JSON 형식으로 반환
    }

    /**
     * 월별 방문자 수 데이터 반환
     * @return JSON 데이터
     */
    @GetMapping("/monthly-visitors")
    public ResponseEntity<List<MonthlyVisitorDTO>> getMonthlyVisitorData() {
        List<MonthlyVisitorDTO> visitorDataList = visitorService.getMonthlyVisitorData();
        return new ResponseEntity<>(visitorDataList, HttpStatus.OK);

}
}
