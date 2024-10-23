package com.damoa.controller;

import com.damoa.service.GoogleSheetsService;
import com.google.api.services.sheets.v4.Sheets;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class GoogleSheetsController {

    private final GoogleSheetsService googleSheetsService;

    /*
     * 주소설계 : http://localhost:8080/reviews/company/import
     * TODO: 주소로 접근하지 않고 스프레드 시트가 업데이트 되면 자동으로 확인되어 업데이트 되게 수정
     *       - Google Apps Script, GCP Pub/Sub 및 Cloud Functions 사용
     * */
    @GetMapping("/reviews/company/import")
    public String companyReviewImport() throws Exception {
        // 1. Google Sheets API 서비스 초기화
        Sheets sheetsService = googleSheetsService.getCloudAccountKey();

        // 2. company Google SpreadSheets ID 및 시트 범위 설정
        String companySpreadsheetId = "12NbvSzLWngg4Vt9G5ZjfPCj-g5mfhY2jrdxTxtLXdQ8";
        String companyRange = "A2:I11";  // 필요한 열 범위를 설정

        // 3. Google Sheets에서 company 리뷰 데이터 가져와 List 변수에 할당
        List<List<Object>> companyValues = sheetsService
                .spreadsheets()
                .values()
                .get(companySpreadsheetId, companyRange)
                .execute()
                .getValues();

        // 4. company 데이터 유효성 검사 및 저장
        if (companyValues != null && !companyValues.isEmpty()) {
            for (List<Object> row : companyValues) {
                googleSheetsService.saveCompanyReview(row);
            }
            googleSheetsService.deleteRowsFromSheet(sheetsService, companySpreadsheetId, companyValues.size(), true);
        } else {
            return "company 리뷰 데이터가 없습니다.";
        }

        return "데이터가 성공적으로 삽입되었습니다.";
    }

    /*
     * 주소설계 : http://localhost:8080/reviews/freelancer/import
     * */
    @GetMapping("/reviews/freelancer/import")
    public String freelancerReviewImport() throws Exception {
        // 1. Google Sheets API 서비스 초기화
        Sheets sheetsService = googleSheetsService.getCloudAccountKey();

        // 2. 프리랜서 Google SpreadSheets ID 및 시트 범위 설정
        String freelancerSpreadsheetId = "12CoTTvlEsD-hJSbVOdwaQsMNneSVvfnMHxt3r24z6Lo";
        String freelancerRange = "A2:I11";  // 필요한 열 범위를 설정

        // 3. Google Sheets에서 프리랜서 데이터 가져와 List 변수에 할당
        List<List<Object>> freelancerValues = sheetsService
                .spreadsheets()
                .values()
                .get(freelancerSpreadsheetId, freelancerRange)
                .execute()
                .getValues();

        // 4. 프리랜서 데이터 유효성 검사 및 저장
        if (freelancerValues != null && !freelancerValues.isEmpty()) {
            for (List<Object> row : freelancerValues) {
                googleSheetsService.saveFreelancerReview(row);
            }
            googleSheetsService.deleteRowsFromSheet(sheetsService, freelancerSpreadsheetId, freelancerValues.size(), false);
        } else {
            return "프리랜서 데이터가 없습니다.";
        }

        return "데이터가 성공적으로 삽입되었습니다.";
    }
}
