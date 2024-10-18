package com.damoa.repository.interfaces;

import com.damoa.repository.model.Faq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface FaqRepository {

    public List<Faq> getAllQna();
    public Faq findById(int id);
    public Faq updateById(int id, @RequestParam String title, @RequestParam String content);
}
