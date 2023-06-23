package com.example.teamtwelvebackend.activity.moodcheckin.controller.ws;

import com.example.teamtwelvebackend.activity.moodcheckin.service.MoodCheckInService;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MoodCheckInHostController {

    private final MoodCheckInService moodCheckInService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/moodcheckin/{roomName}/start")
    @SendTo("/topic/moodcheckin/{roomName}")
    public ActivityRoomMessage start(@DestinationVariable(value = "roomName") String roomName) {
        // TODO 큐알 코드 제공
        return moodCheckInService.getContent(roomName);
    }

    @MessageMapping("/moodcheckin/{roomName}/number")
    @SendTo("/topic/moodcheckin/{roomName}")
    public void number(@DestinationVariable(value = "roomName") String roomName, StompHeaderAccessor stompHeaderAccessor, ActivityRoomMessage activityRoomMessage) {
        log.info("룸네임 " + roomName);
        log.info("메시지 " + activityRoomMessage);
        simpMessageSendingOperations.convertAndSend("/topic/moodcheckin/"+roomName, activityRoomMessage);
    }

    @MessageMapping("/moodcheckin/{roomName}/roading")
    @SendTo("/topic/moodcheckin/{roomName}")
    public ActivityRoomMessage roading(@DestinationVariable(value = "roomName") String roomName, StompHeaderAccessor stompHeaderAccessor, ActivityRoomMessage activityRoomMessage) {
        log.info("룸네임 " + roomName);
        log.info("메시지 " + activityRoomMessage);
        simpMessageSendingOperations.convertAndSend("/topic/moodcheckin/"+roomName, activityRoomMessage);
        // TODO 몇 명 제출했나?
        return moodCheckInService.getContent(roomName);
    }
}
