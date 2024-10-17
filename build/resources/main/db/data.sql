-- 클라이언트 리뷰 데이터 삽입
INSERT INTO clients_review (clients_id, freelancer_id, work_quality_score, timeliness_score, feedback_score, willingness_score, overall_score, review_date, google_form_id, response_data)
VALUES (1, 1, 5, 4, 5, 5, 5, NOW(), 'form123', '{"additional_info": "훌륭한 작업이었습니다. 뽀너스 주머니에 넣어줬습니다."}');
INSERT INTO clients_review (clients_id, freelancer_id, work_quality_score, timeliness_score, feedback_score, willingness_score, overall_score, review_date, google_form_id, response_data)
VALUES (2, 1, 4, 4, 4, 3, 4, NOW(), 'form124', '{"additional_info": "작업이 조금 늦었습니다."}');
INSERT INTO clients_review (clients_id, freelancer_id, work_quality_score, timeliness_score, feedback_score, willingness_score, overall_score, review_date, google_form_id, response_data)
VALUES (3, 2, 5, 5, 5, 4, 5, NOW(), 'form125', '{"additional_info": "상당히 만족스러웠습니다. 소고기 회식 했습니다."}');
INSERT INTO clients_review (clients_id, freelancer_id, work_quality_score, timeliness_score, feedback_score, willingness_score, overall_score, review_date, google_form_id, response_data)
VALUES (1, 3, 3, 2, 3, 3, 3, NOW(), 'form126', '{"additional_info": "평범한 결과였습니다. 클라이언트는 실망했다!"}');

-- 프리랜서 리뷰 데이터 삽입
INSERT INTO freelancers_review (clients_id, freelancer_id, communication_score, timeliness_score, support_score, willingness_score, overall_score, review_date, google_form_id, response_data)
VALUES (1, 1, 5, 4, 5, 5, 5, NOW(), 'form201', '{"additional_info": "클라이언트가 커피 사줍니다."}');
INSERT INTO freelancers_review (clients_id, freelancer_id, communication_score, timeliness_score, support_score, willingness_score, overall_score, review_date, google_form_id, response_data)
VALUES (2, 1, 4, 4, 3, 4, 4, NOW(), 'form202', '{"additional_info": "우리 클라이언트는 소고기도 사줍니다."}');
INSERT INTO freelancers_review (clients_id, freelancer_id, communication_score, timeliness_score, support_score, willingness_score, overall_score, review_date, google_form_id, response_data)
VALUES (3, 2, 5, 5, 5, 4, 5, NOW(), 'form203', '{"additional_info": "좋은 클라이언트 였습니다.."}');
INSERT INTO freelancers_review (clients_id, freelancer_id, communication_score, timeliness_score, support_score, willingness_score, overall_score, review_date, google_form_id, response_data)
VALUES (1, 3, 3, 2, 3, 3, 3, NOW(), 'form204', '{"additional_info": "해당 클라이언트와 함께 하고 싶습니다."}');
