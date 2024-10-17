package com.damoa.service;

import com.damoa.dto.BankAuthDTO;
import com.damoa.repository.interfaces.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // 계좌 등록 신청하기
    public void addAccountReq(BankAuthDTO reqDto){
<<<<<<< HEAD
        System.out.println(reqDto+"서비스 들어옴");
        accountRepository.addBankAccountReq(reqDto.bankCodeStd,
                reqDto.accountNum, reqDto.bankOwner );
=======
        accountRepository.addBankAccountReq(reqDto.getBankCodeStd(),
                reqDto.getAccountNum(), reqDto.getBankOwner());
>>>>>>> f06484ec800a5941ffc3e5ec936a726940748116
    }

}
