package com.damoa.service;

import com.damoa.repository.interfaces.CompanyReviewRepository;
import com.damoa.repository.interfaces.FreelancerReviewRepository;
import com.damoa.repository.model.CompanyReview;
import com.damoa.repository.model.FreelancerReview;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class GoogleSheetsService {

    private final CompanyReviewRepository companyReviewRepo;
    private final FreelancerReviewRepository freelancerReviewRepo;
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // Logger 초기화
    private static final Logger logger = LoggerFactory.getLogger(GoogleSheetsService.class);

    // Google Sheets API 인증 설정
    public Sheets getCloudAccountKey() throws Exception {

        // 파일 주소는 상대경로 사용(팀원들의 절대경로가 모두 다르기 때문)
        InputStream filePath = new FileInputStream("../Damoa/src/main/resources/google_sheet_api/google_sheets.json");
        Credential credential = GoogleCredential.fromStream(filePath)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));  // SCOPES 사용

        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /*
     * CompanyReview 데이터 DB 저장 기능
     * @param row: [reviewDate, work_quality_score, timeliness_score, +
     * feedback_score, willingness_score, response_data]
     * ※ 구글 시트에 저장 된 0행의 reviewDate는 형식이 맞지 않아 사용 x.
     * */
    public void saveCompanyReview(List<Object> row) {

        int companyId = 1;        // 클라이언트 아이디
        int freelancerId = 100;   // 프리랜서 아이디

        try {
            // 각 점수 계산
            int workQualityScore = parseScore(row.get(1));  // work_quality_score
            int timelinessScore = parseScore(row.get(2));   // timeliness_score
            int feedbackScore = parseScore(row.get(3));     // feedback_score
            int willingnessScore = parseScore(row.get(4));  // willingness_score

            // 평균 점수 계산
            int overallScore = (workQualityScore + timelinessScore + feedbackScore + willingnessScore) / 4;

            // 현재 시간으로 review_date 설정
            Timestamp reviewDate = new Timestamp(System.currentTimeMillis());

            CompanyReview review = new CompanyReview(
                    null,
                    companyId,
                    freelancerId,
                    workQualityScore,
                    timelinessScore,
                    feedbackScore,
                    willingnessScore,
                    overallScore,
                    reviewDate,  // 서버에서 리뷰 작성 시간을 저장
                    row.get(5) != null ? row.get(5).toString() : null // response_data
            );
            companyReviewRepo.insertCompanyReview(review);

        } catch (NumberFormatException e) {
            logger.error("유효하지 않은 점수 형식: {}", e.getMessage());
        }
    }

    // 프리랜서 리뷰 데이터 DB 저장 기능
    public void saveFreelancerReview(List<Object> row) {
        int companyId = 1;        // 클라이언트 아이디
        int freelancerId = 100;   // 프리랜서 아이디

        try {
            // 각 점수 계산
            int communicationScore = parseScore(row.get(1)); // communication_score
            int timelinessScore = parseScore(row.get(2));   // timeliness_score
            int supportScore = parseScore(row.get(3));      // support_score
            int willingnessScore = parseScore(row.get(4));   // willingness_score

            // 평균 점수 계산
            int overallScore = (communicationScore + timelinessScore + supportScore + willingnessScore) / 4;

            // 현재 시간으로 review_date 설정
            Timestamp reviewDate = new Timestamp(System.currentTimeMillis());

            FreelancerReview freelancerReview = new FreelancerReview(
                    null,
                    companyId,
                    freelancerId,
                    communicationScore,
                    timelinessScore,
                    supportScore,
                    willingnessScore,
                    overallScore,
                    reviewDate,  // 서버에서 리뷰 작성 시간을 저장
                    row.get(5) != null ? row.get(5).toString() : null // response_data
            );
            freelancerReviewRepo.insertFreelancerReview(freelancerReview);  // 프리랜서 리뷰 데이터 저장

        } catch (NumberFormatException e) {
            logger.error("유효하지 않은 점수 형식: {}", e.getMessage());
        }
    }


    // Pair 1-1. 리뷰 별점 점수를 string -> 숫자형식로 변환하는 메서드
    private int parseScore(Object score) {
        if (score == null || !(score instanceof String)) {
            throw new NumberFormatException("점수는 null이 아닌 문자열이어야 합니다.");
        }

        String scoreStr = (String) score;

        // isNumeric() 메서드 호출
        if (!isNumeric(scoreStr)) {
            throw new NumberFormatException("점수는 숫자 형식이어야 합니다: " + scoreStr);
        }

        return Integer.parseInt(scoreStr);
    }

    // Pair 1-2. 문자열이 정수인지 확인하는 메서드
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // 구글 시트의 행을 삭제해주는 메서드
    public void deleteRowsFromSheet(Sheets sheetsService, String spreadsheetId, int rowCount, boolean isCompanyReview) throws Exception {
        // 구글 시트 gid=ID
        // TODO: 시트 ID와 스프레드시트 ID secure.yml에 저장 필요
        int sheetId = isCompanyReview ? 958507219 : 91723201; // 클라이언트 리뷰인 경우 클라이언트 ID, 아닐 경우 프리랜서 ID
        int startRow = 1; //삭제할 시작 행 (0부터 시작하므로 A2는 인덱스 1)

        // 행 삭제를 위한 요청 설정
        DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest()
                .setRange(new DimensionRange()  // DimensionRange를 사용
                        .setSheetId(sheetId) // 시트 ID
                        .setDimension("ROWS") // 행 삭제
                        .setStartIndex(startRow)
                        .setEndIndex(startRow + rowCount)); // 삭제할 끝 행 (rowCount 만큼)

        // 요청을 batch로 설정
        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(new Request().setDeleteDimension(deleteRequest)));

        // 요청 실행
        sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
        // 삭제 성공 로그
        log.info("Google Sheets의 행이 성공적으로 삭제되었습니다.");
    }

}
