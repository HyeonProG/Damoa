package com.damoa.controller;

import com.damoa.dto.chat.ChatListDTO;
import com.damoa.repository.model.ChatList;
import com.damoa.repository.model.ChatMessage;
import com.damoa.repository.model.User;
import com.damoa.service.ChatListService;
import com.damoa.service.ChatMessageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/match")
@RequiredArgsConstructor
@Log4j2
public class ChatController {

    private final ChatListService chatListService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate; // STOMP 메시지 전송을 위한 템플릿 클래스

    @GetMapping("/my-project")
    public String myProjectPage(){

        return null;
    }

    /**
     * 클라이언트가 "/app/chat" 경로로 보낸 메시지를 처리
     * @param chatMessage 클라이언트가 보낸 채팅 메시지
     * @return 클라이언트에게 다시 전송할 채팅 메시지
     */
    @MessageMapping("/chat/{senderId}/{receiverId}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {

        String senderId = chatMessage.getSenderId();
        String receiverId = chatMessage.getReceiverId();

        // message, senderId, receiverId를 MongoDB에 저장
        chatMessageService.saveMessage(chatMessage, senderId, receiverId);

        messagingTemplate.convertAndSend("/topic/messages/" + receiverId, chatMessage);

        // 클라이언트 간의 연결을 로그에 남김
        log.info("receiver 아이디 {}", receiverId);
        log.info("클라이언트 {}이 클라이언트 {}에게 메시지를 보냄: {}", senderId, receiverId, chatMessage);

        return chatMessage;
    }

    // 채팅 페이지를 반환하는 메서드
    @GetMapping("/chat")
    public String chatPage() {

        return "layout/chat";
    }

    /*
    * sender, receiver ID 테이블 저장 기능
    * 대화 요청 버튼에 해당 기능 삽입예정
    * 주소설계: http://localhost:8080/match/chat-request
    * */
    @PostMapping("/chat-request")
    @ResponseBody
    public String saveChatRequest(@RequestParam int senderId,
                                  @RequestParam int receiverId) {

        chatListService.saveByChatList(senderId, receiverId);

        return "채팅 요청이 완료되었습니다.";
    }

    //채팅 목록을 반환 하는 메서드
    @GetMapping("/chat-list")
    @ResponseBody
    public List<ChatListDTO> chatList(HttpSession session, Model model) {
        // 로그인 된 유저 ID 가졍
        User user = (User) session.getAttribute("principal");

        // 서비스의 메서드를 이용해 userName과 list 데이터를 가져옴
        List<ChatListDTO> chatList = chatListService.findByChatList(user.getId());

        model.addAttribute("chatList", chatList);

        return chatList;
    }
}
