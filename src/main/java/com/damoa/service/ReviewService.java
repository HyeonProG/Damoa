package com.damoa.service;

import com.damoa.repository.interfaces.CompanyReviewRepository;
import com.damoa.repository.interfaces.FreelancerReviewRepository;
import com.damoa.repository.model.CompanyReview;
import com.damoa.repository.model.FreelancerReview;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor    // DI : 의존성 주입 & final
public class ReviewService {

    private final CompanyReviewRepository companyReviewRepo;
    private final FreelancerReviewRepository freelancerReviewRepo;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    // 클라이언트 프리랜서 리뷰 데이터 응답
    public void getAllReviews(Model model) {
        // 1. 클라이언트와 프리랜서 데이터 가져옴
        List<CompanyReview> companyReview = companyReviewRepo.findAllByCompanyReviews();
        List<FreelancerReview> freelancerReview = freelancerReviewRepo.findAllByFreelancerReviews();

        // 3. 모델에 List로 반환
        model.addAttribute("companyReview", companyReview);
        model.addAttribute("freelancerReview",freelancerReview);
        // 토탈 리뷰 갯수
        model.addAttribute("totalReviews", companyReview.size() + freelancerReview.size());
    }

    // 프리랜서 리뷰 데이터 호출 메서드
    // getAllReviews()는 모든 리뷰 데이터를 호출
    // 불필요한 데이터 로드를 피함으로 서버 리소스 소모, 응답 시간을 줄임.
    public void getFreelancerReviews(Model model) {
        List<FreelancerReview> freelancerReview = freelancerReviewRepo.findAllByFreelancerReviews();
        model.addAttribute("freelancerReview", freelancerReview);
        model.addAttribute("totalReviews", freelancerReview.size());
    }

    // 회사 리뷰 데이터 호출 메서드
    public void getCompanyReviews(Model model) {
        List<CompanyReview> companyReview = companyReviewRepo.findAllByCompanyReviews();
        model.addAttribute("companyReview", companyReview);
        model.addAttribute("totalReviews", companyReview.size());
    }

    // 프리랜서 리뷰 상세 조회 기능
    public void getByFreelancerId(int id, Model model) {
            Optional<FreelancerReview> freelancerReviewreview = freelancerReviewRepo.findByFreelancerReviewId(id);
            freelancerReviewreview.ifPresentOrElse(
                    freelancerReview -> model.addAttribute("freelancerReview",freelancerReview),
                    () -> { throw new NullPointerException("삭제된 리뷰 입니다."); }
            );
    }

    // 회사 리뷰 상세 조회 기능
    public void getByCompanyId(int id, Model model) {
        Optional<CompanyReview> companyReview = companyReviewRepo.findByCompanyReviewId(id);
        companyReview.ifPresentOrElse(
                review -> model.addAttribute("companyReview", review),
                () -> { throw new NullPointerException("삭제된 리뷰 입니다."); }
        );
    }

}
