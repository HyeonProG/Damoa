package com.damoa.repository.interfaces;

import com.damoa.dto.TossHistoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentHistoryRepository {
    // 결제 내역 데이터베이스에 추가
    int insertTossHistory(TossHistoryDTO dto);

    List<TossHistoryDTO> viewAll();
}
