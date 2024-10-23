package com.damoa.controller;

import com.damoa.dto.ProjectSaveDTO;
import com.damoa.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private  HttpSession session;
    @Autowired
    private  ProjectService projectService;

    public ProjectController(HttpSession session, ProjectService projectService) {
        this.session = session;
        this.projectService = projectService;
    }

    @GetMapping("/save")
    public String savePage(){

        return "project/save_form";

    }

    @PostMapping("/save")
    public String saveProc(@ModelAttribute("reqDTO") ProjectSaveDTO reqDTO){
        projectService.createProject(reqDTO);
        return "project/save_complete";
    }



}
