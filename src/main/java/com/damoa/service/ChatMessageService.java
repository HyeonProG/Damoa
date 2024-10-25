package com.damoa.service;

import com.damoa.repository.interfaces.ChatMessageRepository;
import com.damoa.repository.interfaces.UserRepository;
import com.damoa.repository.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    /**
     * 클라이언트가 WebSocket 연결 시 처리하는 메서드
     * @param message 클라이언트가 보낸 채팅 메시지
     * @return 저장된 메시지
     */
    public void saveMessage(ChatMessage message, String senderId, String receiverId) {

        message.setTimestamp(Instant.now().toString());
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);

        log.info("메세지 내용: {}", message);
        log.info("발신자: {}", senderId);
        log.info("수신자: {}", receiverId);

        // 생성된 메시지를 DB에 저장
        chatMessageRepository.save(message);
    }
}
