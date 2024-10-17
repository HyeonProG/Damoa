package com.damoa.controller;

import com.damoa.repository.model.Notice;
import com.damoa.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class NoticeController {

    @Autowired
    public HttpSession session;

    @Autowired
    public NoticeService noticeService;

    @GetMapping("/notice/{currentPageNum}")
    public String noticeListPage(@PathVariable(required = false) int currentPageNum,Model model){
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
        return "/user/user_notice_list";
    }

    @GetMapping("/notice/detail/{id}")
    public String noticeDetailPage(@PathVariable("id")int id,  Model model){
        Notice notice = noticeService.getNotice(id);

        model.addAttribute("notice",notice);
        System.out.println(notice);
        return "/user/user_notice_detail";
    }

}
