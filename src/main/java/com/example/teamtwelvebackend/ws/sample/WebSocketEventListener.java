package com.example.teamtwelvebackend.ws.sample;

import com.example.teamtwelvebackend.ws.speedgame.message.ActivityRoomMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessagingTemplate template;

    @EventListener
    public void handleSessionConnectEvent(SessionConnectEvent sessionConnectEvent) {
        Message<byte[]> message = sessionConnectEvent.getMessage();
        log.info(message.toString());
    }

    @EventListener
    public void handleSessionConnectedEvent(SessionConnectedEvent sessionConnectedEvent) {
        Message<byte[]> message = sessionConnectedEvent.getMessage();
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        log.info(message.toString());
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        Message<byte[]> message = sessionSubscribeEvent.getMessage();
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        String simpDestination = (String) header.getHeader("simpDestination");
        String simpSessionId = (String) header.getHeader("simpSessionId");
        template.convertAndSend(simpDestination, new ActivityRoomMessage("ack_user", simpSessionId + "님이 입장했습니다.", "{}"));
        log.info(message.toString());
    }
}
