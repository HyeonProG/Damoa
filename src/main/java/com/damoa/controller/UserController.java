package com.damoa.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.damoa.dto.user.AddSocialUserInfoDTO;
import com.damoa.dto.user.GoogleResponseDTO;
import com.damoa.dto.user.GoogleTokenDTO;
import com.damoa.dto.user.KakaoResponseDTO;
import com.damoa.dto.user.KakaoTokenDTO;
import com.damoa.dto.user.PrincipalDTO;
import com.damoa.dto.user.UserSignInDTO;
import com.damoa.dto.user.UserSignUpDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.model.User;
import com.damoa.service.UserService;
import java.io.PrintWriter;
import java.io.IOException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    // 카카오
    @Value("${kakao.client-id}")
    private String kakaoClientId;
    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;
    private KakaoResponseDTO kakaoResponseDTO;

    // 구글
    @Value("${google.client-id}")
    private String googleClientId;
    @Value("${google.client-secret}")
    private String googleClientSecret;
    @Value("${google.grant-type}")
    private String googleGrantType;
    @Value("${google.redirect-uri}")
    private String googleRedirectUri;
    private GoogleResponseDTO googleResponseDTO;

    private String accessToken = "";
    private String httpHeader = "Bearer" + accessToken;

    /**
     * 회원가입 페이지
     * 
     * @param model
     * @return
     */
    @GetMapping("/sign-up")
    private String signUpPage(Model model) {
        // 카카오 로그인 URL 설정
        String kakaoLoginUrl = kakaoLoginUrl();
        model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);
        model.addAttribute("kakaoRestApiKey", kakaoClientId);
        model.addAttribute("kakaoRedirectUri", kakaoRedirectUri);

        // 구글 로그인 URL 설정
        String googleLoginUrl = googleLoginUrl();
        model.addAttribute("googleLoginUrl", googleLoginUrl);
        model.addAttribute("googleClientId", googleClientId);
        model.addAttribute("googleRedirectUri", googleRedirectUri);

        model.addAttribute("socialType", "local");

        return "/user/sign_up";
    }

    // 카카오 로그인 URL 설정
    private String kakaoLoginUrl() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectUri;
    }

    // 구글 로그인 URL 설정
    private String googleLoginUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUri + "&response_type=code&scope=email profile&access_type=offline";
    }

    /**
     * 이메일 중복 체크
     * 
     * @param email
     * @return
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, String>> checkDuplicateEmail(@RequestParam("email") String email) {
        Map<String, String> response = new HashMap<>();

        int result = userService.checkDuplicateEmail(email);

        if (result != 0) {
            System.out.println("중복된 이메일 작동");
            response.put("message", "중복된 이메일은 사용할 수 없습니다.");
            return ResponseEntity.badRequest().body(response);
        } else {
            response.put("message", "사용가능한 이메일 입니다.");
            return ResponseEntity.ok(response);
        }

    }

    /**
     * 휴대폰 인증 기능
     * coolsms
     * 
     * @param phoneNumber
     * @return
     */
    @GetMapping("/sendSms")
    public @ResponseBody String sendSMS(@RequestParam("phoneNumber") String phoneNumber) {
        Random rand = new Random();
        String numStr = "";
        for (int i = 0; i < 4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }

        System.out.println("수신자 번호 : " + phoneNumber);
        System.out.println("인증번호 : " + numStr);
        userService.certifiedPhoneNumber(phoneNumber, numStr);
        return numStr;
    }

    /**
     * 휴대폰 번호 중복 체크
     * 
     * @param phoneNumber
     * @return
     */
    @GetMapping("/checkPhoneNumber")
    @ResponseBody
    public Map<String, Boolean> checkPhoneNumber(@RequestParam(name = "phoneNumber") String phoneNumber) {
        boolean exists = userService.findDuplicatePhoneNumber(phoneNumber);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return response;
    }

    /**
     * 회원가입 로직
     * 
     * @param dto
     * @return
     */
    @PostMapping("/sign-up")
    public String signUpProc(UserSignUpDTO dto) {
        // 유효성 검사
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new DataDeliveryException("이메일을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            throw new DataDeliveryException("아이디를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new DataDeliveryException("비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        userService.createUser(dto);
        return "redirect:/user/sign-in";
    }

    // 소셜 로그인

    /**
     * 카카오 로그인 API
     * 
     * @param code
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/kakao")
    public String KakaoLoginPage(@RequestParam(name = "code", required = false) String code,
            HttpServletResponse response, HttpSession session) throws IOException {
        // Access Token 발급 요청
        RestTemplate rt1 = new RestTemplate();
        HttpHeaders header1 = new HttpHeaders();
        header1.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);
        HttpEntity<MultiValueMap<String, String>> reqkakaoToken = new HttpEntity<>(params, header1);
        ResponseEntity<KakaoTokenDTO> response1 = rt1.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,
                reqkakaoToken, KakaoTokenDTO.class);
        accessToken = response1.getBody().getAccessToken();

        // Access Token으로 유저 정보 받아오기
        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + response1.getBody().getAccessToken());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<MultiValueMap<String, String>> reqKakaoMessage = new HttpEntity<>(headers2);
        ResponseEntity<KakaoResponseDTO> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
                reqKakaoMessage, KakaoResponseDTO.class);
        kakaoResponseDTO = response2.getBody();

        // 최초 소셜 사용자인지 판별
        PrincipalDTO principalDTO = userService.findByUserEmail(kakaoResponseDTO.getKakaoAccount().getEmail());
        if (principalDTO == null) {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html; charset=utf-8");
            out.println("<script>alert('추가 정보를 입력해주세요.')</script>");
            out.flush();
            return "/user/add_kakao_user_info"; // 추가 정보를 입력하는 페이지로 이동
        }

        session.setAttribute("principal", principalDTO);
        return "redirect:/main";
    }

    /**
     * 카카오 소셜 로그인 추가 정보 요구 페이지
     * 
     * @param addKakaoUserInfoDTO
     * @param session
     * @return
     */
    @PostMapping("/add-kakao-user-info")
    public String addKakaoUserInfo(AddSocialUserInfoDTO addKakaoUserInfoDTO, HttpSession session) {
        UserSignUpDTO userSignUpDTO = UserSignUpDTO.builder()
                .email(kakaoResponseDTO.getKakaoAccount().getEmail())
                .phoneNumber(addKakaoUserInfoDTO.getPhoneNumber())
                .username(addKakaoUserInfoDTO.getUsername())
                .socialType(addKakaoUserInfoDTO.getSocialType())
                .userType(addKakaoUserInfoDTO.getUserType())
                .password("1234") // 기본 비밀번호
                .build();

        userService.createUser(userSignUpDTO);
        System.out.println("userSignUpDTO : " + userSignUpDTO.toString());

        // 추가로 이메일을 세션에 저장
        PrincipalDTO principalDTO = userService.findByUserEmail(addKakaoUserInfoDTO.getEmail());
        session.setAttribute("principal", principalDTO);

        return "redirect:/user/sign-in";
    }

    /**
     * 구글 소셜 로그인 API
     * 
     * @param code
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/google")
    public String GoogleLoginPage(@RequestParam(name = "code") String code, HttpServletResponse response, HttpSession session)
            throws IOException {
        // Access Token 발급 요청
        RestTemplate rt1 = new RestTemplate();
        HttpHeaders header1 = new HttpHeaders();
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<String, String>();
        params1.add("client_id", googleClientId);
        params1.add("client_secret", googleClientSecret);
        params1.add("code", code);
        params1.add("grant_type", googleGrantType);
        params1.add("redirect_uri", googleRedirectUri);
        HttpEntity<MultiValueMap<String, String>> reqgoogleMessage = new HttpEntity<>(params1, header1);
        ResponseEntity<GoogleTokenDTO> response1 = rt1.exchange("https://oauth2.googleapis.com/token", HttpMethod.POST,
                reqgoogleMessage, GoogleTokenDTO.class);
        accessToken = response1.getBody().getAccessToken();

        // Access Token으로 유저 정보 받아오기
        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + accessToken);
        HttpEntity<MultiValueMap<String, String>> reqGoogleInfoMessage = new HttpEntity<>(headers2);
        ResponseEntity<GoogleResponseDTO> response2 = rt2.exchange(
                "https://www.googleapis.com/userinfo/v2/me?access_token=" + accessToken, HttpMethod.GET,
                reqGoogleInfoMessage,
                GoogleResponseDTO.class);
        googleResponseDTO = response2.getBody();

        // 최초 소셜 사용자인지 판별
        PrincipalDTO principalDTO = userService.findByUserEmail(googleResponseDTO.getEmail());
        if (principalDTO == null) {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html; charset=utf-8");
            out.println("<script>alert('추가 정보를 입력해주세요.')</script>");
            out.flush();
            return "/user/add_google_user_info"; // 추가 정보를 입력하는 페이지로 이동
        }

        session.setAttribute("principal", principalDTO);
        return "redirect:/main";

    }

    /**
     * 구글 소셜 로그인 추가 정보 요구 페이지
     * 
     * @param addGoogleUserInfoDTO
     * @param session
     * @return
     */
    @PostMapping("/add-google-user-info")
    public String addGoogleUserInfo(AddSocialUserInfoDTO addGoogleUserInfoDTO, HttpSession session) {
        UserSignUpDTO userSignUpDTO = UserSignUpDTO.builder()
                .email(googleResponseDTO.getEmail())
                .phoneNumber(addGoogleUserInfoDTO.getPhoneNumber())
                .username(addGoogleUserInfoDTO.getUsername())
                .socialType(addGoogleUserInfoDTO.getSocialType())
                .userType(addGoogleUserInfoDTO.getUserType())
                .password("12341234") // 기본 비밀번호
                .build();

        userService.createUser(userSignUpDTO);
        System.out.println("userSignUpDTO : " + userSignUpDTO.toString());

        // 추가로 이메일을 세션에 저장
        PrincipalDTO principalDTO = userService.findByUserEmail(addGoogleUserInfoDTO.getEmail());
        session.setAttribute("principal", principalDTO);

        return "redirect:/user/sign-in";
    }

    /**
     * 로그인 페이지
     * 
     * @return
     */
    @GetMapping("/sign-in")
    public String signInPage(Model model) {
        // 카카오 로그인 URL 설정
        String kakaoLoginUrl = kakaoLoginUrl();

        // 구글 로그인 URL 설정
        String googleLoginUrl = googleLoginUrl();

        model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);
        model.addAttribute("googleLoginUrl", googleLoginUrl);

        return "/user/sign_in";
    }

    @PostMapping("sign-in")
    public String signInProc(UserSignInDTO userSignInDTO, Model model, HttpServletRequest request) {
        // 카카오 로그인 URL 설정
        String kakaoLoginUrl = kakaoLoginUrl();

        // 구글 로그인 URL 설정
        String googleLoginUrl = googleLoginUrl();

        model.addAttribute("kakaoLoginUrl", kakaoLoginUrl);
        model.addAttribute("googleLoginUrl", googleLoginUrl);

        try {
            User user = userService.findUser(userSignInDTO);

            // 세션 생성
            HttpSession session = request.getSession(true);
            session.setAttribute("principal", user);

            return "redirect:/main";
        } catch (Exception e) {
            if (userSignInDTO.getEmail() == null || userSignInDTO.getEmail().isEmpty()) {
                throw new DataDeliveryException("이메일을 입력하세요.", HttpStatus.BAD_REQUEST);
            }
            if (userSignInDTO.getPassword() == null || userSignInDTO.getPassword().isEmpty()) {
                throw new DataDeliveryException("비밀번호를 입력하세요.", HttpStatus.BAD_REQUEST);
            }
            model.addAttribute("errorMessage", e.getMessage());
            return "/user/sign_in";
        }

    }

    /**
     * 로그아웃
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("principal");
        if (user == null) {
            return "redirect:/user/sign-in";
        }

        PrincipalDTO principalDTO = PrincipalDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .userType(user.getUserType())
                .socialType(user.getSocialType())
                .build();

        model.addAttribute("user", principalDTO);
        return "user/mypage";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(HttpSession session) {
        User user = (User) session.getAttribute("principal");
        if (user == null) {
            return "redirect:/user/sign-in";
        }
        // 회원탈퇴 처리
        userService.deleteUserAccount(user);
        session.invalidate();
        return "redirect:/";
    }

}
