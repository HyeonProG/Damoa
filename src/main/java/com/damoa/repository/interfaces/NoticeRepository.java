package com.damoa.repository.interfaces;

import com.damoa.repository.model.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeRepository {

    public List<Notice> getNoticeList(@Param("limit")int limit, @Param("offset") int offset);
    public List<Notice> getAllNotice();
    public Notice getNotice(int id);
}
