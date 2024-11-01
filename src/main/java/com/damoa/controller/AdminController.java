package com.damoa.controller;

import com.damoa.dto.TossHistoryDTO;
import com.damoa.dto.admin.*;
import com.damoa.dto.user.MonthlyRegisterDTO;
import com.damoa.dto.user.MonthlyVisitorDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.model.Ad;
import com.damoa.repository.model.Admin;
import com.damoa.repository.model.Notice;
import com.damoa.repository.model.User;
import com.damoa.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import com.damoa.dto.DailyCompanyReviewDTO;
import com.damoa.dto.DailyFreelancerReviewDTO;
import com.damoa.dto.MonthlyFreelancerDTO;
import com.damoa.dto.MonthlyProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Value("${file.upload-dir-ad}")
    private String uploadAddir;

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
    private final NoticeService noticeService;

    /**
     * 관리자 메인 페이지
     *
     * @return
     */
    @GetMapping("/main")
    public String mainPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Admin admin = session != null ? (Admin) session.getAttribute("admin") : null;

        // 로그인을 하지 않았을 경우 로그인 페이지로 리다이렉트
        if (admin == null) {
            return "redirect:/admin/sign-in";
        }

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
    @PostMapping("/sign-in")
    public String adminSignInProc(AdminSignInDTO adminSignInDTO, HttpServletRequest request) {
        try {
            Admin admin = adminService.findAdmin(adminSignInDTO);
            // 세션 생성
            HttpSession session = request.getSession(true);
            session.setAttribute("admin", admin);
            return "redirect:/admin/main";
        } catch (Exception e) {
            if (adminSignInDTO.getUsername() == null || adminSignInDTO.getUsername().isEmpty()) {
                throw new DataDeliveryException("아이디를 입력하세요.", HttpStatus.BAD_REQUEST);
            }
            if (adminSignInDTO.getPassword() == null || adminSignInDTO.getPassword().isEmpty()) {
                throw new DataDeliveryException("비밀번호를 입력하세요.", HttpStatus.BAD_REQUEST);
            }
            e.printStackTrace();
            return "/admin/sign_in";
        }
    }

    @GetMapping("/management/{currentPageNum}")
    public String UserListPage(@PathVariable(required = false) Integer currentPageNum, Model model) {

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

    /**
     * 모든 결제 내역
     *
     * @param model
     * @return
     */
    @GetMapping("/management")
    public String paymemtHistoryPage(@PageableDefault(size = 5) Pageable pageable, Model model) {
        // 결제 내역 조회
        Page<TossHistoryDTO> paymentPage = payService.findAll(pageable);

        // 날짜 포맷팅을 위한 DateTimeFormatter 설정
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 숫자 포맷팅을 위한 NumberFormat 설정
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        // 각 결제 내역의 requestedAt과 amount를 포맷팅하여 새로운 리스트 생성
        List<TossHistoryDTO> formattedPaymentList = paymentPage.stream()
                .map(payment -> formatPayment(payment, inputFormatter, outputFormatter, numberFormat))
                .collect(Collectors.toList());

        // 페이지 정보를 계산하여 모델에 추가
        int currentPage = paymentPage.getNumber(); // 현재 페이지 번호 (0부터 시작)
        model.addAttribute("currentPage", currentPage + 1); // 페이지를 1부터 시작하기 위해 +1
        model.addAttribute("totalPages", paymentPage.getTotalPages()); // 전체 페이지 수 추가
        model.addAttribute("nextPage", currentPage + 1 < paymentPage.getTotalPages() ? currentPage + 1 : null); // 다음 페이지 (+2)
        model.addAttribute("prevPage", currentPage > 0 ? currentPage - 1 : null); // 이전 페이지 번호
        model.addAttribute("pagination", paymentPage);
        model.addAttribute("paymentList", formattedPaymentList);

        return "/admin/admin_management_payment";
    }

    /**
     * 환불
     *
     * @param model
     * @return
     */
    @GetMapping("/refund")
    public String refundApprovalPage(@PageableDefault(size = 5) Pageable pageable, Model model) {
        // 결제 내역 조회
        Page<TossHistoryDTO> paymentPage = payService.findRequestedRefund(pageable);

        // 날짜 포맷팅을 위한 DateTimeFormatter 설정
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 숫자 포맷팅을 위한 NumberFormat 설정
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        // 각 결제 내역의 requestedAt과 amount를 포맷팅하여 새로운 리스트 생성
        List<TossHistoryDTO> formattedPaymentList = paymentPage.stream()
                .map(payment -> formatPayment(payment, inputFormatter, outputFormatter, numberFormat)) // 메서드 호출
                .collect(Collectors.toList());

        // 페이지 정보를 계산하여 모델에 추가
        int currentPage = paymentPage.getNumber(); // 현재 페이지 번호 (0부터 시작)
        model.addAttribute("currentPage", currentPage + 1); // 페이지를 1부터 시작하기 위해 +1
        model.addAttribute("totalPages", paymentPage.getTotalPages()); // 전체 페이지 수 추가
        model.addAttribute("nextPage", currentPage + 1 < paymentPage.getTotalPages() ? currentPage + 1 : null); // 다음 페이지 (+2)
        model.addAttribute("prevPage", currentPage > 0 ? currentPage - 1 : null); // 이전 페이지 번호
        model.addAttribute("pagination", paymentPage);
        model.addAttribute("paymentList", formattedPaymentList);

        return "/admin/admin_refund_approval";
    }

    /**
     * http://localhost:8080/admin/list/company
     *
     * @param model
     * @return
     */
    @GetMapping("/list/company/{currentPageNum}") // URL의 {type} 부분을 변수로 처리
    public String companyReviewList(@PathVariable(required = false) Integer currentPageNum, Model model) {
        int pageSize = 6;
        int offset;

        int totallist = reviewService.countReview(); // 총몇개의 row 인지 확인
        int totalPages = (int) Math.ceil((double) totallist / (double) pageSize); // 2.1 = 13 / 6

        if (currentPageNum == null || currentPageNum <= 1) {
            currentPageNum = 2;
            offset = 0;
        } else {
            offset = (currentPageNum - 1) * pageSize;
        }
        int nextPageNum;
        int beforePageNum;

        if (currentPageNum >= totalPages - 1 && totalPages > 3) {
            currentPageNum = totalPages - 1;
            nextPageNum = currentPageNum + 1;
            beforePageNum = currentPageNum - 1;
        } else if (currentPageNum >= totalPages - 1) {
            currentPageNum = 2;
            nextPageNum = 3;
            beforePageNum = 1;
        }

        List<CompanyReviewDTO> list = reviewService.getComapanyReviews(pageSize, offset);

        model.addAttribute("list", list);
        model.addAttribute("totallist", totallist);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPageNum", currentPageNum);
        model.addAttribute("beforePageNum", currentPageNum - 1);
        model.addAttribute("nextPageNum", currentPageNum + 1);

        return "admin/company_list";
    }

    @GetMapping("/list/freelancer/{currentPageNum}")
    public String freelancerReviewList(@PathVariable(required = false) Integer currentPageNum, Model model) {
        int pageSize = 6;
        int offset;

        int totallist = reviewService.countFreelancerReview(); // 총몇개의 row 인지 확인
        int totalPages = (int) Math.ceil((double) totallist / (double) pageSize); // 2.1 = 13 / 6

        if (currentPageNum == null || currentPageNum <= 1) {
            currentPageNum = 2;
            offset = 0;
        } else {
            offset = (currentPageNum - 1) * pageSize;
        }
        int nextPageNum;
        int beforePageNum;

        if (currentPageNum >= totalPages - 1 && totalPages > 3) {
            currentPageNum = totalPages - 1;
            nextPageNum = currentPageNum + 1;
            beforePageNum = currentPageNum - 1;
        } else if (currentPageNum >= totalPages - 1) {
            currentPageNum = 2;
            nextPageNum = 3;
            beforePageNum = 1;
        }
        List<FreelancerReviewDTO> list = reviewService.findFreelancerReview(pageSize, offset);

        model.addAttribute("list", list);
        model.addAttribute("totallist", totallist);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPageNum", currentPageNum);
        model.addAttribute("beforePageNum", currentPageNum - 1);
        model.addAttribute("nextPageNum", currentPageNum + 1);

        return "admin/freelancer_list";
    }


    /**
     * 월별 프로젝트 등록 수 데이터 반환
     *
     * @return
     */
    @GetMapping("/monthly-projects")
    public ResponseEntity<List<MonthlyProjectDTO>> getMonthlyProjectData() {
        List<MonthlyProjectDTO> projectDataList = projectService.getMonthlyProjectData();
        return new ResponseEntity<>(projectDataList, HttpStatus.OK);
    }

    /**
     * 월별 프리랜서 등록 수 데이터 반환
     *
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

    /**
     * 결제 내역의 amount를 포맷팅하는 메서드
     *
     * @param payment 결제 내역 DTO
     * @return 포맷팅된 결제 내역 DTO
     */
    // 결제 내역 포맷팅 메서드
    private TossHistoryDTO formatPayment(TossHistoryDTO payment, DateTimeFormatter inputFormatter,
                                         DateTimeFormatter outputFormatter, NumberFormat numberFormat) {
        // String 타입의 requestedAt 필드 포맷팅
        String originalDateStr = payment.getRequestedAt();
        String formattedDate = OffsetDateTime.parse(originalDateStr, inputFormatter)
                .format(outputFormatter);
        payment.setRequestedAt(formattedDate);

        // amount를 쉼표가 포함된 형식으로 포맷팅
        if (payment.getAmount() != null) { // amount가 null이 아닐 경우만 포맷팅
            try {
                double amountValue = Double.parseDouble(payment.getAmount());
                String formattedAmount = numberFormat.format(amountValue);
                payment.setAmount(formattedAmount);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // 숫자 변환 시 예외 발생 시 로그 출력
                // 예외가 발생하면 포맷팅하지 않고 그대로 유지
            }
        }

        return payment; // 포맷팅된 결제 내역 반환
    }

    @GetMapping("/notice/{currentPageNum}")
    public String noticeListPage(@PathVariable(name = "currentPageNum", required = false) int currentPageNum,Model model){
        // 모든 공지 가져오기
        List<Notice> allNotice = noticeService.getAllNotice();
        int totalNotice = allNotice.size(); // 모든 공지 개수
        int limit = 10; // 한 페이지 당 공지 수
        int totalPages = totalNotice/limit; // 총 페이지 수
        int offset; // 시작하는 게시글 id

        if(currentPageNum == 0 || currentPageNum == 1){
            currentPageNum = 1;
            offset = 0;
        } else {
            offset = currentPageNum*limit+1;
        }

        System.out.println("limit"+limit);
        System.out.println("offset"+offset);

        // 공지 가져오기 (페이징 처리)
        List<Notice> noticeList = noticeService.getNoticeList(limit,offset);

        // 뷰에 데이터 전송
        model.addAttribute("noticeList",noticeList);
        model.addAttribute("totalNotice",totalNotice);
        model.addAttribute("totalPages",totalPages);

        if(currentPageNum<=1){
            model.addAttribute("currentPageNum",1);
            model.addAttribute("lastPage",1);
            model.addAttribute("nextPage",3);
        } else{
            model.addAttribute("currentPageNum",currentPageNum);
            model.addAttribute("lastPage",currentPageNum-1);
            model.addAttribute("nextPage",currentPageNum+1);
        }
        return "/admin/notice";
    }

    @GetMapping("/notice/detail/{id}")
    public String noticeDetailPage(@PathVariable("id")int id,  Model model){
        Notice notice = noticeService.getNotice(id);

        model.addAttribute("notice",notice);
        System.out.println(notice);
        return "/admin/notice_detail";
    }

}
