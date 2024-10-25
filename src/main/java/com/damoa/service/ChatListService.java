package com.damoa.service;

import com.damoa.dto.chat.ChatListDTO;
import com.damoa.repository.interfaces.ChatListRepository;
import com.damoa.repository.interfaces.UserRepository;
import com.damoa.repository.model.ChatList;
import com.damoa.repository.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatListService {

    private final UserRepository userRepository;
    private final ChatListRepository chatListRepository;


    // senderId, receiverId 저장 기능
    public void saveByChatList(int senderId, int receiverId) {
        ChatList chatList = new ChatList();

        chatList.setSenderId(senderId);
        chatList.setReceiverId(receiverId);

        chatListRepository.saveByChatList(chatList);
    }

    /*
     * 채팅 목록과 receiver 이름 반환 기능
     * @param userId 세션에 저장된 로그인 유저의 ID
     * @return 채팅 목록과 각 receiver의 이름을 포함한 DTO 리스트
     */
    public List<ChatListDTO> findByChatList(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        // 1. userId와 관련된 채팅 목록을 가져옴 (해당 유저가 참여한 채팅)
        List<ChatList> chatLists = chatListRepository.findByChatList(userId);

        // 2. Stream을 사용해서 chatList 객체를 ChatListDTO로 변환
        List<ChatListDTO> collect = chatLists.stream()
                .map(chatList -> {
                    User receiver = userRepository.findById(chatList.getReceiverId());
                    return new ChatListDTO(receiver.getUsername(), chatList);    // map 반환문
                })
                .collect(Collectors.toList());
        return collect;
    }
}
