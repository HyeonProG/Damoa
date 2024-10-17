package com.damoa.repository.interfaces;

import com.damoa.repository.model.CompanyReview;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/*
* company 리뷰 기능
* */
@Mapper
public interface CompanyReviewRepository {

    // company 리뷰 데이터 호출
    List<CompanyReview> findAllByCompanyReviews();

    // GCP company 리뷰 데이터 호출 후 MySQL로 데이터 insert
    void insertCompanyReview(CompanyReview companyReview);
}
