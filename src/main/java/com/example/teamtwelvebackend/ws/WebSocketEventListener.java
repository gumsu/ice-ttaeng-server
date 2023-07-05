package com.example.teamtwelvebackend.ws;

import com.example.teamtwelvebackend.CustomJwtAuthenticationToken;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

import java.security.Principal;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessagingTemplate template;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final SimpUserRegistry simpUserRegistry;

    @EventListener
    public void handleSessionConnectEvent(SessionConnectEvent sessionConnectEvent) {
        Message<byte[]> message = sessionConnectEvent.getMessage();
        log.info("SessionConnectEvent " + message.toString());
    }

    @EventListener
    public void handleSessionConnectedEvent(SessionConnectedEvent sessionConnectedEvent) {
        Message<byte[]> message = sessionConnectedEvent.getMessage();
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        log.info("SessionConnectedEvent " + message);
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        Message<byte[]> message = sessionSubscribeEvent.getMessage();
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        String simpDestination = (String) header.getHeader("simpDestination");
        String simpSessionId = (String) header.getHeader("simpSessionId");
        template.convertAndSend(simpDestination, new ActivityRoomMessage("ack_user", simpSessionId + "님이 입장했습니다.", "{}"));
        log.info("SessionSubscribeEvent " + message);


        Principal simpUser = sessionSubscribeEvent.getUser();

        if (simpUser instanceof CustomJwtAuthenticationToken jwt) {
            Set<SimpSubscription> subscriptions = simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals(simpDestination));

            RoomParticipantChangedMessage payload = new RoomParticipantChangedMessage(jwt.getNickname(), subscriptions.size());
            template.convertAndSend(simpDestination+"/user-count", new ActivityRoomMessage("ack_user", jwt.getNickname() + "님이 입장했습니다.", payload));

        } else if (simpUser instanceof Participant participant) {
            Set<SimpSubscription> subscriptions = simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals(simpDestination));
            RoomParticipantChangedMessage payload = new RoomParticipantChangedMessage(participant.nickname, subscriptions.size());
            template.convertAndSend(simpDestination+"/user-count", new ActivityRoomMessage("ack_user", participant.nickname + "님이 입장했습니다.", payload));
        }

        log.info("allUser: " + simpUserRegistry.getUserCount());
//        log.info("subscriptions: " + subscriptions.size());
    }


    @EventListener
    public void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        Message<byte[]> message = event.getMessage();
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        String simpDestination = (String) header.getHeader("simpDestination");
        String simpSessionId = (String) header.getHeader("simpSessionId");
//        template.convertAndSend(simpDestination, new ActivityRoomMessage("ack_user", simpSessionId + "님이 입장했습니다.", "{}"));
        log.info("SessionSubscribeEvent " + message.toString());

        Principal simpUser = event.getUser();

        if (simpUser instanceof CustomJwtAuthenticationToken jwt) {
            String destination = jwt.removeDestinationBySubscriptionId(header.getSubscriptionId());

            Set<SimpSubscription> subscriptions = simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals(destination));
            RoomParticipantChangedMessage payload = new RoomParticipantChangedMessage(jwt.getNickname(), subscriptions.size());
            template.convertAndSend(destination+"/user-count", new ActivityRoomMessage("ack_user", jwt.getNickname() + "님이 퇴장했습니다.", payload));

        } else if (simpUser instanceof Participant participant) {
            String destination = participant.removeDestinationBySubscriptionId(header.getSubscriptionId());

            Set<SimpSubscription> subscriptions = simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals(destination));
            RoomParticipantChangedMessage payload = new RoomParticipantChangedMessage(participant.nickname, subscriptions.size());
            template.convertAndSend(destination+"/user-count", new ActivityRoomMessage("ack_user", participant.nickname + "님이 퇴장했습니다.", payload));
        }
        log.info("allUser: " + simpUserRegistry.getUserCount());
//        log.info("subscriptions: " + subscriptions.size());
    }

    @EventListener
    public void handleSessionDisconnectedEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        Message<byte[]> message = sessionDisconnectEvent.getMessage();
        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        String simpDestination = (String) header.getHeader("simpDestination");
        String simpSessionId = (String) header.getHeader("simpSessionId");

        Principal simpUser = sessionDisconnectEvent.getUser();

        if (simpUser instanceof CustomJwtAuthenticationToken jwt) {
            jwt.getDestinations().forEach((id, destination) -> {
                Set<SimpSubscription> subscriptions = simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals(destination));
                RoomParticipantChangedMessage payload = new RoomParticipantChangedMessage(jwt.getNickname(), subscriptions.size());
                template.convertAndSend(destination+"/user-count", new ActivityRoomMessage("ack_user", jwt.getNickname() + "님이 퇴장했습니다.", payload));
            });
        } else if (simpUser instanceof Participant participant) {
            participant.getDestinations().forEach((id, destination) -> {
                Set<SimpSubscription> subscriptions = simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals(destination));
                RoomParticipantChangedMessage payload = new RoomParticipantChangedMessage(participant.nickname, subscriptions.size());
                template.convertAndSend(destination+"/user-count", new ActivityRoomMessage("ack_user", participant.nickname + "님이 퇴장했습니다.", payload));
            });
        }

        // TODO 세션 끊기면 실시간 인원수 줄어듦 + 삭제
//        template.convertAndSend("/disconnect/"+header.getSessionId(), new ActivityRoomMessage("ack_user", simpSessionId + "님이 퇴장했습니다.", "{}"));
//        simpMessageSendingOperations.convertAndSend("/disconnect/"+header.getSessionId(), header.getSessionId());
        log.info("SessionDisconnectEvent " + message.toString());
    }
}
