package com.damoa.repository.interfaces;

import com.damoa.repository.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/*
 * MongoDB Document
 * 채팅 메세지 기록
 * */
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

}
