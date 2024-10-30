# <다모아 : 1인 개발자-기업 간 양방향 매칭 플랫폼>
### Springboot와 Mustache를 사용한 웹사이트 제작
&nbsp; 
&nbsp;
![제목을-입력해주세요_-001 (1)](https://github.com/user-attachments/assets/49932d05-6613-4d87-b322-278c1029f0f7)




&nbsp;
### 목차
1. 프로젝트 개요
2. 구성원 및 맡은 역할
3. 서비스 환경
4. 사용 라이브러리 및 외부 API
5. 사이트 맵 (유저, 관리자)
6. 주요 기능
7. ERD 다이어그램
&nbsp; &nbsp;&nbsp;
## 1️⃣ 프로젝트 개요
### (1) 프로젝트 주제 및 목적

### (2) 프로젝트 핵심 기능
&nbsp; 
## 2️⃣ 구성원 및 맡은 역할
|이름|역할|맡은 역할|
|------|---|---|
|조현재|팀장| 프로젝트 총괄 및 팀장, 프리랜서 등록 및 검색 기능, 회원가입, 로그인 기능 구  |
|김가령|팀원| 회원 탈퇴, 마이페이지 관리, 관리자 대시보드 구현 |
|변영준|팀원| TossAPI를 사용한 결제/환불 기능, 네이버 클로바 기반 챗봇, 공지사항 관리 기능 구현 |
|엄송현|팀원| 프로젝트 등록 및 검색 기능, 전자 서명 및 계약서 등록, 메인 페이지 UI 구현 |
|정해주|팀원| FaQ 등록 및 관리 기능, 유저 관리 기능, 리뷰 관리 기능, 광고 관리 기능 구현 |
## 3️⃣ 서비스 환경 
|유형|구분|서비스 배포 환경|
|------|---|---|
|SW|OS| Windows10 |
||Browser| Chrome 121.0.6167.161 |
||Tool| IntelliJ |
||BackEnd| Java 17 & MySQL 8.0.26 & h2 & redis & MyBatis |
||Template Engine| Mustache |
||Version/Issue 관리| GitHub & GitBash |
||Communication| Discord & Notion |

## 4️⃣ 사용 라이브러리 및 외부 API
### (1) 사용 라이브러리
|라이브러리 명|버전|용도|
|------|---|---|
|Lombok|---|---|
|devtools|---|---|
|spring-security|---|---|
|Spring-starter-WebSocket|---|---|
|STOMP|---|---|
|Json|---|---|

### (2) 사용 외부 API
|기능|API 명|제공|용도|
|------|---|---|---|
|로그인|카카오 로그인|-----|-----|
|로그인|구글 로그인|-----|-----|
|결제 및 환불|Toss 결제 / 결제 취소 API|-----|-----|
|리뷰|구글 폼 API|-----|-----|
|리뷰|구글 스프레드시트 API|-----|-----|

## 4️⃣ 사이트맵


## 5️⃣ ERD 다이어그램



## 6️⃣ 주요 기능 및 화면 소개 &nbsp;
### 1. 사용자 (기업/프리랜서)
#### (1) 로그인 및 회원가입 
#### (2) 메인 화면
#### (3) 프로젝트 등록
#### (4) 프리랜서 등록
#### (5) 프로젝트 찾기 (목록)
#### (6) 프로젝트 찾기 (디테일)
![img1 daumcdn](https://github.com/user-attachments/assets/09fe2b11-6879-4db2-b25e-b4ebb3cb5a19)

#### (7) 프리랜서 찾기 (목록)
#### (8) 프리랜서 찾기 (디테일)
#### (9) 1:1 채팅 기능
#### (10) 캐쉬 충전 
#### (11) 리뷰 게시판 (목록)
#### (12) 리뷰 게시판 (디테일)
#### (13) FaQ 게시판 (목록)
#### (14) FaQ 게시판 (디테일)
#### (15) 공지사항 게시판 (목록)
#### (16) 공지사항 게시판 (디테일)
#### (17) 마이 페이지
#### 프로젝트 = 내 공고 정보
#### 프리랜서 - 내 정보 수정
#### 포인트 충전 내역 조회
#### 1:1 채팅봇 : 모아봇
#### 회원 탈퇴


### 2. 관리자 
#### (1) 로그인
#### 대쉬보드 (관리자 메인 페이지)
#### 회원 관리
#### 결제 관리
#### 환불 관리
#### 광고 관리
#### 고객 지원
#### 기업 리뷰
#### 프리랜서 리뷰
