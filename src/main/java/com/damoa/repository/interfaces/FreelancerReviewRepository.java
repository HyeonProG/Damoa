package com.damoa.repository.interfaces;

import com.damoa.dto.admin.FreelancerReviewDTO;
import com.damoa.repository.model.FreelancerReview;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Optional;

/*
* 프리랜서 리뷰 기능
* */
@Mapper
public interface FreelancerReviewRepository {

    // 프리랜서 리뷰 데이터 호출
    List<FreelancerReview> findAllByFreelancerReviews();

    // 프리랜서 리뷰 id pk 호출
    Optional<FreelancerReview> findByFreelancerReviewId(int id);

    // GCP 프리랜서 리뷰 데이터 실행 후 MySQL로 데이터 insert
    void insertFreelancerReview(FreelancerReview freelancerReview);

    List<FreelancerReviewDTO> findFreelancerReview(int limit, int offset);

    int countFreelancerReview();
}
