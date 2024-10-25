package com.damoa.service;

import com.damoa.dto.TossApproveResponse;
import com.damoa.dto.TossHistoryDTO;
import com.damoa.repository.interfaces.PaymentHistoryRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired
    private final PaymentHistoryRepository paymentHistoryRepository;

    public String getOrderId() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DATE);
        Random random1 = new Random();
        Random random2 = new Random();
        int rdValue1 = random1.nextInt(100);
        int rdValue2 = random1.nextInt(100);

        // 랜덤아이디를 만들기 위해 인트를 스트링으로 변환
        String yStr = Integer.toString(y);
        String mStr = Integer.toString(m);
        String dStr = Integer.toString(d);
        String randomStr1 = Integer.toString(rdValue1);
        String randomStr2 = Integer.toString(rdValue2);

        return mStr + randomStr1 + yStr + randomStr2 + dStr;
    }


    @Transactional
    public void insertTossHistory(TossApproveResponse response, int principalId) {
        int result = 0;
        TossHistoryDTO dto = TossHistoryDTO.builder()
                .paymentKey(response.getPaymentKey())
                .userId(principalId)
                .orderId(response.getOrderId())
                .orderName(response.getOrderName())
                .amount(response.getTotalAmount())
                .method(response.getMethod())
                .requestedAt(response.getRequestedAt())
                .approvedAt(response.getApprovedAt())
                .build();
        result = paymentHistoryRepository.insertTossHistory(dto);


        if (result != 1) {
            System.out.println("결제 실패 했어요..");
        }

    }

    public TossHistoryDTO findPaymentHistory(int id) {

        TossHistoryDTO entity = paymentHistoryRepository.findById(id);

        return entity;
    }

    public List<TossHistoryDTO> findAll() {

        List<TossHistoryDTO> entity = paymentHistoryRepository.viewAll();

        return entity;
    }

    @Transactional
    public void updatePoint(String amountStr, Integer id) {

        int currentPoint = paymentHistoryRepository.findPointById(id);
        int amount = currentPoint + Integer.parseInt(amountStr);
        paymentHistoryRepository.updateUserPoint(amount, id);
    }

    public List<TossHistoryDTO> findRequestedRefund() {
        List<TossHistoryDTO> dto = paymentHistoryRepository.findHistoryByStatus();
        return dto;
    }
}
