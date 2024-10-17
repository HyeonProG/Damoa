package com.damoa.service;

import com.damoa.repository.interfaces.NoticeRepository;
import com.damoa.repository.model.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Autowired
    public NoticeRepository noticeRepository;

    // 공지 목록 가져오기
    public List<Notice> getNoticeList (int limit, int offset){
        return noticeRepository.getNoticeList(limit,offset);
    }

    // 공지 다 가져오기
    public List<Notice> getAllNotice (){
        return noticeRepository.getAllNotice();
    }
    
    // 공지 세부사항 가져오기
    public Notice getNotice(int id){
        return noticeRepository.getNotice(id);
    }
}
