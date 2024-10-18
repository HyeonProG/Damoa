package com.damoa.controller;

import com.damoa.repository.model.Faq;
import com.damoa.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/faq")
public class FaqController {

    @Autowired
    public FaqService faqService;


    @GetMapping("/list")
    public String qnaListPage(Model model){
        List<Faq> faqList = faqService.getAllQna();
        model.addAttribute("faqList",faqList);
        return "admin/admin_faq_list";

    }

    @GetMapping("/detail/{id}")
    public String detailPage(@PathVariable int id, Model model){
        Faq faq = faqService.getFaqById(id);
        model.addAttribute("faq",faq);
        return "admin/admin_faq_detail";
    }

    @GetMapping("/update/{id}")
    public String updatePage(@PathVariable("id") int id, Model model){
        Faq faq = faqService.getFaqById(id);
        model.addAttribute("faq",faq);
        return "admin/admin_faq_update";
    }

    @PostMapping("/update/{id}")
    public String updatePage(@RequestParam int id,@RequestParam String title, @RequestParam String content){
        faqService.updateById(id,title,content);
        faqService.getFaqById(id);
        return "admin/admin_faq_update";
    }



}
