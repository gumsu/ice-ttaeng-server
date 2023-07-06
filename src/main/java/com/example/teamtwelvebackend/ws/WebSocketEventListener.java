package com.example.teamtwelvebackend.ws;

import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

import java.security.Principal;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {
    final ParticipantService participantService;

    private final SimpMessagingTemplate template;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @EventListener
    public void handleSessionConnectEvent(SessionConnectEvent sessionConnectEvent) {
        Message<byte[]> message = sessionConnectEvent.getMessage();
        log.info("SessionConnectEvent " + message);
    }

    @EventListener
    public void handleSessionConnectedEvent(SessionConnectedEvent sessionConnectedEvent) {
        Message<byte[]> message = sessionConnectedEvent.getMessage();
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        log.info("SessionConnectedEvent " + message);
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        Message<byte[]> message = event.getMessage();
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        String destination = (String) header.getHeader("simpDestination");
        log.info("SessionSubscribeEvent " + message);

        if (event.getUser() instanceof ActivityParticipant participant) {
            List<ActivityParticipant> all = participantService.getAll(destination);
            RoomParticipantChangedMessage payload = new RoomParticipantChangedMessage(participant.getNickname(), all.size());
            template.convertAndSend(destination+"/user-count", new ActivityRoomMessage("ack_user", participant.getNickname() + "님이 입장했습니다.", payload));
        }
    }


    @EventListener
    public void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        Message<byte[]> message = event.getMessage();
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        log.info("SessionSubscribeEvent " + message);

        if (event.getUser() instanceof ActivityParticipant participant) {
            String destination = participant.removeDestinationBySubscriptionId(header.getSubscriptionId());
            List<ActivityParticipant> all = participantService.getAll(destination);
            RoomParticipantChangedMessage payload = new RoomParticipantChangedMessage(participant.getNickname(), all.size());
            template.convertAndSend(destination+"/user-count", new ActivityRoomMessage("ack_user", participant.getNickname() + "님이 퇴장했습니다.", payload));
        }
    }

    @EventListener
    public void handleSessionDisconnectedEvent(SessionDisconnectEvent event) {
        log.info("SessionDisconnectEvent " + event.getMessage());

        if (event.getUser() instanceof ActivityParticipant participant) {
            participant.getDestinations().forEach((id, destination) -> {
                List<ActivityParticipant> all = participantService.getAll(destination);
                RoomParticipantChangedMessage payload = new RoomParticipantChangedMessage(participant.getNickname(), all.size());
                template.convertAndSend(destination+"/user-count", new ActivityRoomMessage("ack_user", participant.getNickname() + "님이 퇴장했습니다.", payload));
            });
        }

        // TODO 세션 끊기면 실시간 인원수 줄어듦 + 삭제
//        template.convertAndSend("/disconnect/"+header.getSessionId(), new ActivityRoomMessage("ack_user", simpSessionId + "님이 퇴장했습니다.", "{}"));
//        simpMessageSendingOperations.convertAndSend("/disconnect/"+header.getSessionId(), header.getSessionId());

    }
}
