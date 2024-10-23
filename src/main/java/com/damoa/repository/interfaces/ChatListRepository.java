package com.damoa.repository.interfaces;

import com.damoa.repository.model.ChatList;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/*
* MySQL 매퍼
* chat_list_tb
* */
@Mapper
public interface ChatListRepository {

    Optional<ChatList> findByChatListId(int id);

    /*
    * function: chat_list_tb insert
    * @param companyId, freelancerId
    * */
    void saveByChatList(ChatList chatList);
}
