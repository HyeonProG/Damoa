package com.damoa.controller;

import com.damoa.repository.model.ChatMessage;
import com.damoa.service.ChatListService;
import com.damoa.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/match")
@RequiredArgsConstructor
public class ChatController {

    private final ChatListService chatListService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate; // STOMP 메시지 전송을 위한 템플릿 클래스

    /**
     * 클라이언트가 "/app/chat" 경로로 보낸 메시지를 처리
     * @param chatMessage 클라이언트가 보낸 채팅 메시지
     * @return 클라이언트에게 다시 전송할 채팅 메시지
     */
    @MessageMapping("/chat") // 클라이언트에서 "/app/chat" 경로로 들어오는 메시지를 처리
    public ChatMessage sendMessage(SimpMessageHeaderAccessor headerAccessor, ChatMessage chatMessage) {
        // 연결 처리 및 메시지 저장
        chatMessageService.connectAndSaveMessage(headerAccessor, chatMessage);

        // STOMP 메시지 브로드캐스팅
        messagingTemplate.convertAndSend("/topic/messages", chatMessage);

        return chatMessage; // 채팅 메시지를 반환하여 구독자에게 전송
    }

    // 채팅 페이지를 반환하는 메서드
    @GetMapping("/chat")
    public String chatPage() {


        return "layout/chat";
    }

    /*
    * Test페이지 호출
    * */
    @GetMapping("/test-page")
    public String testPage() {

        return "match/requestTest";
    }

    /*
    * sender, receiver ID 테이블 저장 기능
    * */
    @PostMapping("/request")
    @ResponseBody
    public String saveChatRequest(@RequestParam int senderId,
                                  @RequestParam int receiverId) {

        chatListService.saveByChatList(senderId, receiverId);

        return "채팅 요청이 완료되었습니다.";
    }

}
