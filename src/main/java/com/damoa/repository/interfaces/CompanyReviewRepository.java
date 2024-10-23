package com.damoa.repository.interfaces;

import com.damoa.repository.model.CompanyReview;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/*
* company 리뷰 기능
* */
@Mapper
public interface CompanyReviewRepository {

    // company 리뷰 데이터 호출
    List<CompanyReview> findAllByCompanyReviews();

    // company 리뷰 id pk 호출
    Optional<CompanyReview> findByCompanyReviewId(int id);

    // GCP company 리뷰 데이터 호출 후 MySQL로 데이터 insert
    void insertCompanyReview(CompanyReview companyReview);
}
