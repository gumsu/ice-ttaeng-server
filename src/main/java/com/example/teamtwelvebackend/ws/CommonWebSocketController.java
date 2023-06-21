package com.example.teamtwelvebackend.ws;

import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class CommonWebSocketController {

    // TODO 공통으로 퇴장한 사람 체크 필요
    @MessageMapping("/disconnect/{sessionId}")
    public ActivityRoomMessage disconnect(@DestinationVariable(value = "sessionId") String sessionId, StompHeaderAccessor stompHeaderAccessor, ActivityRoomMessage activityRoomMessage) {
        log.info("세션 아이디 " + sessionId);
        log.info("메시지 " + activityRoomMessage);
        return null;
    }
}
