package com.damoa.service;

import com.damoa.constants.UserType;
import com.damoa.repository.interfaces.ChatMessageRepository;
import com.damoa.repository.interfaces.UserRepository;
import com.damoa.repository.model.ChatMessage;
import com.damoa.repository.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    /**
     * 클라이언트가 WebSocket 연결 시 처리하는 메서드
     * @param headerAccessor WebSocket 연결 요청의 헤더 정보를 담고 있는 객체
     * @param message 클라이언트가 보낸 채팅 메시지
     * @return 저장된 메시지
     */
    public void connectAndSaveMessage(SimpMessageHeaderAccessor headerAccessor, ChatMessage message) {

        // 메시지에 현재 시간을 타임스탬프로 설정
        message.setTimestamp(Instant.now().toString());

        // 두 클라이언트의 세션 ID 가져오기
        String sessionId = headerAccessor.getSessionId();

        // 세션 ID를 기반으로 사용자 조회
        List<User> user = null;

        // 사용자 유형에 따라 회사 ID 또는 프리랜서 ID 설정
        if (UserType.COMPANY.name().equalsIgnoreCase(user.get(0).getUserType())) {
            message.setCompanyId(String.valueOf(user.get(0))); // 회사 ID 설정
        } else {
            message.setFreelancerId(String.valueOf(user.get(0))); // 프리랜서 ID 설정
        }

        // 메시지 내용을 "상대방과 연결 되었습니다."로 설정
        message.setMessage("상대방과 연결 되었습니다.");

        // 생성된 메시지를 DB에 저장
        chatMessageRepository.save(message);
    }
}
