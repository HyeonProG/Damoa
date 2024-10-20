package com.damoa.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.damoa.dto.freelancer.RegisterFreelancerDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.interfaces.FreelancerRepository;
import com.damoa.repository.model.Freelancer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreelancerService {

    @Autowired
    private final FreelancerRepository freelancerRepository;

    /**
     * 프리랜서 등록
     * 
     * @param dto
     * @return
     */
    @Transactional
    public void insertFreelancer(RegisterFreelancerDTO dto) {
        System.out.println("======================");
        System.out.println("CAREER : " + dto.getCareer());
        System.out.println("======================");
        System.out.println("======================");
        System.out.println("UPLOADFILENAME : " + dto.getUploadFileName());
        System.out.println("======================");
        System.out.println("======================");
        System.out.println("ORIGINFILENAME : " + dto.getOriginFileName());
        System.out.println("======================");
        int result = 0;
        try {
            // 파일 업로드 수행
            if (dto.getMFile() != null && !dto.getMFile().isEmpty()) {
                String[] fileNames = uploadFile(dto.getMFile());
                dto.setOriginFileName(fileNames[0]);
                dto.setUploadFileName(fileNames[1]);
            }
            // 프리랜서 등록
            result = freelancerRepository.insertFreelancer(dto.toFreelancer());
            if (result == 0) {
                throw new RuntimeException("프리랜서 등록 실패");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 접근 오류 발생", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("프리랜서 등록 중 오류 발생", e);
        }
    }

    /**
	 * 파일 업로드
	 */
	@Transactional
	public String[] uploadFile(MultipartFile mFile) {
		if (mFile.getSize() > 1024 * 1024 * 20) {
			throw new DataDeliveryException("파일 크기는 20MB 이상 클 수 없습니다.", HttpStatus.BAD_REQUEST);
		}

		String saveDirectory = null;

		try {
			saveDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/portfolio/";
			System.out.println("saveDirectory : " + saveDirectory);
			File dir = new File(saveDirectory);
		    if (!dir.exists()) {
		        dir.mkdirs(); // 디렉토리 생성
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		String originalFileName = mFile.getOriginalFilename();
		String uploadFileName = UUID.randomUUID() + "_" + originalFileName;
		String uploadPath = saveDirectory + File.separator + uploadFileName;
		File destination = new File(uploadPath);
		System.out.println("UPLOADPATH : " + uploadPath);
		System.out.println("destination : " + destination);
		try {
			mFile.transferTo(destination);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new DataDeliveryException("파일 업로드 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new String[] { originalFileName, uploadFileName };
	}

}
