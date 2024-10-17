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

@Log4j2
@Service
@RequiredArgsConstructor    // DI : 의존성 주입 & final
public class ReviewService {

    private final CompanyReviewRepository companyReviewRepo;
    private final FreelancerReviewRepository freelancerReviewRepo;
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

    // 별점 리뷰 점수 평균 계산 기능
    private int calculateOverallScore(CompanyReview review) {
        return (review.getWorkQualityScore() + review.getTimelinessScore() +
                review.getFeedbackScore() + review.getWillingnessScore()) / 4;
    }
}
