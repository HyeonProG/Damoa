package com.damoa.service;

import com.damoa.dto.BankAuthDTO;
import com.damoa.repository.interfaces.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    // 계좌 등록 신청하기
    public void addAccountReq(BankAuthDTO reqDto){
        accountRepository.addBankAccountReq(reqDto.bankCodeStd,
                reqDto.accountNum, reqDto.bankOwner );
    }

}
