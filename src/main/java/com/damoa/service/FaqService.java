package com.damoa.service;

import com.damoa.repository.interfaces.FaqRepository;
import com.damoa.repository.model.Faq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqService {

    @Autowired
    public FaqRepository faqRepository;

    public List<Faq> getAllQna(){
        return faqRepository.getAllQna();
    }

    public Faq getFaqById(int id){
        return faqRepository.findById(id);
    }

    public Faq updateById(int id,String content, String title){
        return  faqRepository.updateById(id,content,title);
    }


}
