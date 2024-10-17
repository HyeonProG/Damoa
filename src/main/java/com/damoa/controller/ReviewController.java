package com.damoa.controller;

import com.damoa.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor    // DI & final
public class ReviewController {

    private final HttpSession session;
    private final ReviewService reviewService;

    // Review 메인 요청 페이지
    // http://localhost:8080/review/home
    // review/home 데이터 요청 메서드
    @GetMapping("/home")
    public String reviewHomePost(Model model) {

        reviewService.getAllReviews(model);

        return "review/home";
    }
}