package com.damoa.controller;

import com.damoa.dto.TossApproveResponse;
import com.damoa.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/toss")
@RequiredArgsConstructor
public class TossController {

    private final PaymentService payService;

    @GetMapping("/pay")
    public String tossPage(Model model) {
        String orderId = payService.getOrderId();
        System.out.println("payService : " + orderId);

        model.addAttribute("amount", 1);
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderName", "결제");
        model.addAttribute("customerName", "주니");
        return "/tossPay";
    }

    @GetMapping("/success")
    public String successPage(@RequestParam(name = "orderId") String orderId, @RequestParam(name = "paymentKey") String paymentKey) throws IOException, InterruptedIOException {
        System.out.println(orderId);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic dGVzdF9za181T1dSYXBkQThkd1JPbTRtTURPbnJvMXpFcVpLOg=="); // basic64 << 인코딩
        headers.add("Content-Type", "application/json");


        // JSON 형식의 요청 본문 생성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("paymentKey", paymentKey);
        requestBody.put("orderId", orderId);
        requestBody.put("amount", "1");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<TossApproveResponse> response = restTemplate.exchange(
                    "https://api.tosspayments.com/v1/payments/confirm", HttpMethod.POST, requestEntity, TossApproveResponse.class);

            TossApproveResponse response2 = response.getBody();

            //TODO 나중에 아이디 수정할 예정
            System.out.println(response2.toString());
             payService.insertTossHistory(response2, 1);

        } catch (HttpClientErrorException e) {
            System.err.println("Error response body: " + e.getResponseBodyAsString());
        }

        return "redirect:/chat";
    }


    @GetMapping("/fail")
    public String fail() {

        return "/chat";
    }
}
