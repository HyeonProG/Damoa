package com.damoa.repository.interfaces;

import com.damoa.repository.model.FreelancerReview;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/*
* 프리랜서 리뷰 기능
* */
@Mapper
public interface FreelancerReviewRepository {

    // 프리랜서 리뷰 데이터 호출
    List<FreelancerReview> findAllByFreelancerReviews();

    // GCP 프리랜서 리뷰 데이터 실행 후 MySQL로 데이터 insert
    void insertFreelancerReview(FreelancerReview freelancerReview);
}
