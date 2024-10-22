package com.damoa.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TossHistoryDTO {

    private int id;
    private String paymentKey;
    private int userId;
    private String orderId;
    private String orderName;
    private String amount;
    private String method;
    private String requestedAt;
    private String approvedAt;
}
