package com.damoa.dto;

import lombok.*;

// 계좌 실명 인증을 위한 DTO
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAuthDTO {

    // 은행별 고유 코드
    private String bankCodeStd;
    // 계좌번호
    private String accountNum;
    // 예금주 명
    private String bankOwner;
}
