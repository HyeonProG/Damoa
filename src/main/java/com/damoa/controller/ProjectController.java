package com.damoa.controller;

import com.damoa.dto.ProjectSaveDTO;
import com.damoa.dto.user.ProjectWaitDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.model.Project;
import com.damoa.repository.model.ProjectWait;
import com.damoa.service.ProjectService;
import com.damoa.utils.Define;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private  HttpSession session;
    @Autowired
    private  ProjectService projectService;

    // DI
    public ProjectController(HttpSession session, ProjectService projectService) {
        this.session = session;
        this.projectService = projectService;
    }

    // 프로젝트 폼 페이지 이동
    @GetMapping("/save")
    public String savePage(){
        return "project/save-form";

    }

    // 프로젝트 폼 저장 요청
    @PostMapping("/save")
    public String saveProc(@ModelAttribute("reqDTO") ProjectSaveDTO reqDTO){
        projectService.createProject(reqDTO);
        return "project/save-complete";
    }

    // 프로젝트 게시판 이동
    @GetMapping("/list/{currentPageNum}")
    public String projectListPage(@PathVariable(required=false)int currentPageNum, Model model){
        // 모든 프로젝트 가져오기
        List<Project> projectList = projectService.getAllProject();
        int totalProjectNum = projectList.size();

        // 페이징 처리
        // limit - 한 페이지에 몇 개의 프로젝트가 들어갈 건가?
        int limit =10;

        // 총 페이지 수
        int totalPageNum = totalProjectNum/limit;

        // offset - 몇 번째 프로젝트부터 볼 것인가
        int offset;
        if(currentPageNum == 0 || currentPageNum == 1){
            currentPageNum = 1;
            offset = 0;
        } else {
            offset=limit*currentPageNum+1;
        }
        // 페이징 처리 된 프로젝트들
        List<Project> projectListForPaging = projectService.getProjectForPaging(limit,offset);

        model.addAttribute("totalPageNum",totalPageNum);
        model.addAttribute("currentPageNum",currentPageNum);
        model.addAttribute("projectList",projectList);

        
        return "project/list";
    }

    /**
     * 프로젝트 상세 보기
     * @param projectId
     * @param model
     * @return
     */
    @GetMapping("/detail/{projectId}")
    public String projectDetailPage(@PathVariable(required=false)int projectId, Model model){
        Project project = projectService.findProjectById(projectId);
        model.addAttribute("project",project);
        return "project/detail";
    }

    @PostMapping("/wait")
    public String makeProjectWait(@ModelAttribute ProjectWaitDTO reqDTO, Model model){
        System.out.println(reqDTO);

        ProjectWait newProWait = reqDTO.toProWait(reqDTO);

        if(reqDTO.getFile() != null){
            // 포트폴리오 파일 저장
            String[] fileNames = uploadFile(reqDTO.getFile());
            newProWait.setOriginFileName(fileNames[0]);
            newProWait.setUploadFileName(fileNames[1]);
        }

        projectService.makeNewWait(newProWait);
        System.out.println(newProWait);

        Project project = projectService.findProjectById(reqDTO.getProjectId());
        model.addAttribute("project",project);
        return "project/detail";
    }

    /**
     * 파일 업로드 및 이름 암호화
     * @param mFile
     * @return
     */
    private String[] uploadFile(MultipartFile mFile){
        if(mFile.getSize()> Define.MAX_FILE_SIZE){
            throw new DataDeliveryException("파일 크기는 20MB보다 클 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 서버 컴퓨터 - 디렉토리 체크
        String saveDirectory = Define.UPLOAD_FILE_DIRECTORY_FOR_PORTFOLIO;
        File directory = new File(saveDirectory);
        if(!directory.exists()){
            directory.mkdirs();
        }

        // 파일명 중복 방지를 위한 파일명
        String uploadFileName = UUID.randomUUID()+"_"+mFile.getOriginalFilename();

        // 파일 전체 경ㄹ + 새로 생성한 파일명
        String uploadPath = saveDirectory+uploadFileName;
        File destination = new File(uploadPath);

        try {
            mFile.transferTo(destination);
        } catch (IllegalStateException | IOException e){
            e.printStackTrace();
            throw new DataDeliveryException("파일 업로드 중에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new String[] {mFile.getOriginalFilename(), uploadFileName};
    }



}
