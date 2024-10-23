package com.damoa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket 설정 클래스
 * WebSocketMessageBrokerConfigurer 인터페이스를 구현하여 메시지 브로커 및 STOMP 엔드포인트를 설정
 */
@Configuration // Spring의 설정 클래스임을 나타냄
@EnableWebSocketMessageBroker // WebSocket 메시지 브로커를 활성화 (STOMP 프로토콜 사용)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 메시지 브로커 구성
     * @param config 메시지 브로커 설정을 위한 MessageBrokerRegistry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 간단한 메시지 브로커 활성화: 클라이언트로 메시지를 전달하기 위한 목적지 지정 ("/topic")
        config.enableSimpleBroker("/topic");

        // 클라이언트에서 서버로 전송되는 메시지의 목적지 접두사 설정
        // "/app"로 시작하는 메시지는 서버 측에서 처리됨
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * STOMP 엔드포인트를 등록
     * @param registry STOMP 엔드포인트 설정을 위한 StompEndpointRegistry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 연결을 위한 엔드포인트 "/ws"를 등록
        // SockJS 지원을 추가하여 WebSocket을 지원하지 않는 브라우저에서도 연결 가능
        registry.addEndpoint("/ws").withSockJS();
    }
}
