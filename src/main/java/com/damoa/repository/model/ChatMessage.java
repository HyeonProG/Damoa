package com.damoa.repository.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// MongoDB Document 모델
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages") // DB 컬렉션 네임
public class ChatMessage {
    @Id
    private String id;                  // 채팅방 ID
    private String companyId;           // 클라이언트 ID, userID의 user_type이 'company'라면 세션아이디 저장
    private String freelancerId;        // 프리랜서 ID
    private String message;
    private String timestamp;

}
