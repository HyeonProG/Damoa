package com.damoa.service;

import com.damoa.repository.interfaces.ChatListRepository;
import com.damoa.repository.model.ChatList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatListService {

    private final ChatListRepository chatListRepository;

    public void saveByChatList(int senderId, int receiverId) {
        ChatList chatList = new ChatList();

        chatList.setCompanyId(senderId);
        chatList.setFreelancerId(receiverId);

        chatListRepository.saveByChatList(chatList);
    }
}
