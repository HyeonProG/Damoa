package com.damoa.controller;

import com.damoa.dto.TossHistoryDTO;
import com.damoa.dto.admin.CompanyReviewDTO;
import com.damoa.dto.admin.FreelancerReviewDTO;
import com.damoa.dto.user.MonthlyRegisterDTO;
import com.damoa.dto.user.MonthlyVisitorDTO;
import com.damoa.repository.model.Admin;
import com.damoa.repository.model.User;
import com.damoa.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import com.damoa.dto.DailyCompanyReviewDTO;
import com.damoa.dto.DailyFreelancerReviewDTO;
import com.damoa.dto.MonthlyFreelancerDTO;
import com.damoa.dto.MonthlyProjectDTO;
import com.damoa.dto.user.MonthlyRegisterDTO;
import com.damoa.dto.user.MonthlyVisitorDTO;
import com.damoa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.damoa.dto.admin.AdminSignInDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.model.Admin;
import com.damoa.repository.model.User;

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

    @Autowired
    private final VisitorService visitorService;

    @Autowired
    private final ReviewService reviewService;

    @Autowired
    private final ProjectService projectService;

    @Autowired
    private final PaymentService payService;

    @Autowired
    private final FreelancerService freelancerService;

    @Autowired
    private final ReviewService reviewService;

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
//
//    @GetMapping("/management")
//    public String ListPage(Model model){
//        List<User> userList = adminService.getAllUser();
//        model.addAttribute("userList", userList);
//        return "admin/admin_user_list";
//    }
    @GetMapping("/management/{currentPageNum}")
    public String UserListPage(@PathVariable(required = false)Integer currentPageNum, Model model){

        List<User> allUser = adminService.getAllUser();
        int totalUser = allUser.size();
        int limit = 10;
        int totalPages = totalUser / limit;
        int offset;

        if (currentPageNum == null || currentPageNum <= 1) {
            currentPageNum = 2;
            offset = 0;
        } else {
            offset = limit * (currentPageNum - 1);
        }


        List<User> userList = adminService.getUserList(limit, offset);

        model.addAttribute("userList", userList);
        model.addAttribute("totalUser", totalUser);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPageNum", currentPageNum);
        model.addAttribute("beforePageNum", currentPageNum - 1);
        model.addAttribute("nextPageNum", currentPageNum + 1);

        return "/admin/admin_user_list";
    }


    /**
     * 월별 회원 수 데이터 반환
     *
     * @return
     */
    @GetMapping("/monthly-registers")
    public ResponseEntity<List<MonthlyRegisterDTO>> getMonthlyRegisterData() {
        List<MonthlyRegisterDTO> registerDataList = adminService.getMonthlyRegisterData();
        return new ResponseEntity<>(registerDataList, HttpStatus.OK); // JSON 형식으로 반환
    }

    /**
     * 월별 방문자 수 데이터 반환
     *
     * @return JSON 데이터
     */
    @GetMapping("/monthly-visitors")
    public ResponseEntity<List<MonthlyVisitorDTO>> getMonthlyVisitorData() {
        List<MonthlyVisitorDTO> visitorDataList = visitorService.getMonthlyVisitorData();
        return new ResponseEntity<>(visitorDataList, HttpStatus.OK);

    }

    @GetMapping("/management")
    public String paymemtHistoryPage(Model model) {
        List<TossHistoryDTO> paymentList = payService.findAll();


        model.addAttribute("paymentList", paymentList);
        return "/admin/admin_management_payment";
    }

    @GetMapping("/refund")
    public String refundApprovalPage(Model model) {
        List<TossHistoryDTO> paymentList = payService.findRequestedRefund();


        model.addAttribute("paymentList", paymentList);
        return "/admin/admin_refund_approval";
    }

    /**
     *  http://localhost:8080/admin/list/company
     * @param model
     * @return
     */
    @GetMapping("/list/company/{currentPageNum}") // URL의 {type} 부분을 변수로 처리
    public String companyReviewList(@PathVariable(required = false)Integer currentPageNum, Model model) {
        int pageSize = 6;
        int offset;

        int totallist = reviewService.countReview(); // 총몇개의 row 인지 확인
        int totalPages = (int) Math.ceil((double) totallist / (double) pageSize); //  2.1 = 13 / 6

        if(currentPageNum == null || currentPageNum <= 1){
            currentPageNum = 2;
            offset = 0;
        } else{
            offset = (currentPageNum -1) * pageSize;
        }
        int nextPageNum;
        int beforePageNum;

        if(currentPageNum >= totalPages -1 && totalPages > 3){
            currentPageNum = totalPages -1;
            nextPageNum = currentPageNum +1;
            beforePageNum = currentPageNum -1;
        } else if (currentPageNum >= totalPages -1){
            currentPageNum = 2;
            nextPageNum = 3;
            beforePageNum = 1;
        }

            List<CompanyReviewDTO> list = reviewService.getComapanyReviews(pageSize,offset);

        model.addAttribute("list",list);
        model.addAttribute("totallist",totallist);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("currentPageNum",currentPageNum);
        model.addAttribute("beforePageNum",currentPageNum -1);
        model.addAttribute("nextPageNum",currentPageNum +1);

        return "admin/company_list";
    }

    @GetMapping("/list/freelancer/{currentPageNum}")
    public String freelancerReviewList(@PathVariable(required = false)Integer currentPageNum, Model model){
        int pageSize = 6;
        int offset;

        int totallist = reviewService.countFreelancerReview(); // 총몇개의 row 인지 확인
        int totalPages = (int) Math.ceil((double) totallist / (double) pageSize); //  2.1 = 13 / 6

        if(currentPageNum == null || currentPageNum <= 1){
            currentPageNum = 2;
            offset = 0;
        } else{
            offset = (currentPageNum -1) * pageSize;
        }
        int nextPageNum;
        int beforePageNum;

        if(currentPageNum >= totalPages -1 && totalPages > 3){
            currentPageNum = totalPages -1;
            nextPageNum = currentPageNum +1;
            beforePageNum = currentPageNum -1;
        } else if (currentPageNum >= totalPages -1){
            currentPageNum = 2;
            nextPageNum = 3;
            beforePageNum = 1;
        }
        List<FreelancerReviewDTO> list = reviewService.findFreelancerReview(pageSize,offset);

        model.addAttribute("list",list);
        model.addAttribute("totallist",totallist);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("currentPageNum",currentPageNum);
        model.addAttribute("beforePageNum",currentPageNum -1);
        model.addAttribute("nextPageNum",currentPageNum +1);

        return "admin/freelancer_list";
    }






}
    /**
     * 월별 프로젝트 등록 수 데이터 반환
     * @return
     */
    @GetMapping("/monthly-projects")
    public ResponseEntity<List<MonthlyProjectDTO>> getMonthlyProjectData() {
        List<MonthlyProjectDTO> projectDataList = projectService.getMonthlyProjectData();
        return new ResponseEntity<>(projectDataList, HttpStatus.OK); 
    }

    /**
     * 월별 프리랜서 등록 수 데이터 반환
     * @return
     */
    @GetMapping("/monthly-freelancers")
    public ResponseEntity<List<MonthlyFreelancerDTO>> getMonthlyFreelancerData() {
        List<MonthlyFreelancerDTO> freelancerDataList = freelancerService.getMonthlyFreelancerData();
        return new ResponseEntity<>(freelancerDataList, HttpStatus.OK);
    }

    // 일별 기업 리뷰 데이터 반환 API
    @GetMapping("/daily-company-reviews")
    public ResponseEntity<List<DailyCompanyReviewDTO>> getDailyCompanyReviewData() {
        List<DailyCompanyReviewDTO> companyReviewDataList = reviewService.getDailyCompanyReviewData();
        return new ResponseEntity<>(companyReviewDataList, HttpStatus.OK);
    }

    // 일별 프리랜서 리뷰 데이터 반환 API
    @GetMapping("/daily-freelancer-reviews")
    public ResponseEntity<List<DailyFreelancerReviewDTO>> getDailyFreelancerReviewData() {
        List<DailyFreelancerReviewDTO> freelancerReviewDataList = reviewService.getDailyFreelancerReviewData();
        return new ResponseEntity<>(freelancerReviewDataList, HttpStatus.OK);
    }
}

