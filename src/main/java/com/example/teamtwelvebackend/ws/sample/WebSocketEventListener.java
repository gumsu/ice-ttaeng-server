package com.example.teamtwelvebackend.ws.sample;

import com.example.teamtwelvebackend.ws.speedgame.message.ActivityRoomMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessagingTemplate template;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @EventListener
    public void handleSessionConnectEvent(SessionConnectEvent sessionConnectEvent) {
        Message<byte[]> message = sessionConnectEvent.getMessage();
        log.info("SessionConnectEvent " + message.toString());
    }

    @EventListener
    public void handleSessionConnectedEvent(SessionConnectedEvent sessionConnectedEvent) {
        Message<byte[]> message = sessionConnectedEvent.getMessage();
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        log.info("SessionConnectedEvent " + message.toString());
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        Message<byte[]> message = sessionSubscribeEvent.getMessage();
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        String simpDestination = (String) header.getHeader("simpDestination");
        String simpSessionId = (String) header.getHeader("simpSessionId");
        template.convertAndSend(simpDestination, new ActivityRoomMessage("ack_user", simpSessionId + "님이 입장했습니다.", "{}"));
        log.info("SessionSubscribeEvent " + message.toString());

        // TODO 접속 인원수 체크하기
        simpMessageSendingOperations.convertAndSend(header.getDestination() + "/number", header.getSessionId());
    }

    @EventListener
    public void handleSessionDisconnectedEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        Message<byte[]> message = sessionDisconnectEvent.getMessage();
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        String simpDestination = (String) header.getHeader("simpDestination");
        String simpSessionId = (String) header.getHeader("simpSessionId");

        // TODO 세션 끊기면 실시간 인원수 줄어듦 + 삭제
//        template.convertAndSend("/disconnect/"+header.getSessionId(), new ActivityRoomMessage("ack_user", simpSessionId + "님이 퇴장했습니다.", "{}"));
//        simpMessageSendingOperations.convertAndSend("/disconnect/"+header.getSessionId(), header.getSessionId());
        log.info("SessionDisconnectEvent " + message.toString());
    }
}
