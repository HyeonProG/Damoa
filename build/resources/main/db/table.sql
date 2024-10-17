-- 클라이언트 리뷰 테이블
CREATE TABLE clients_review (
    id INT AUTO_INCREMENT PRIMARY KEY,
    clients_id INT NOT NULL,
    freelancer_id INT NOT NULL,
    work_quality_score INT NOT NULL CHECK (work_quality_score BETWEEN 1 AND 5), -- 1~5 점수
    timeliness_score INT NOT NULL CHECK (timeliness_score BETWEEN 1 AND 5),
    feedback_score INT NOT NULL CHECK (feedback_score BETWEEN 1 AND 5),
    willingness_score INT NOT NULL CHECK (willingness_score BETWEEN 1 AND 5),
    overall_score INT NOT NULL CHECK (overall_score BETWEEN 1 AND 5),
    review_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    google_form_id VARCHAR(255),               -- 구글 폼 ID
    response_data JSON                         -- JSON 형식으로 응답 데이터 저장
);

-- 프리랜서 리뷰 테이블
CREATE TABLE freelancers_review (
    id INT AUTO_INCREMENT PRIMARY KEY,
    clients_id INT NOT NULL,
    freelancer_id INT NOT NULL,
    communication_score INT NOT NULL CHECK (communication_score BETWEEN 1 AND 5), -- 1~5 점수
    timeliness_score INT NOT NULL CHECK (timeliness_score BETWEEN 1 AND 5),
    support_score INT NOT NULL CHECK (support_score BETWEEN 1 AND 5),
    willingness_score INT NOT NULL CHECK (willingness_score BETWEEN 1 AND 5),
    overall_score INT NOT NULL CHECK (overall_score BETWEEN 1 AND 5),
    review_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    google_form_id VARCHAR(255),               -- 구글 폼 ID
    response_data JSON                         -- JSON 형식으로 응답 데이터 저장
);
