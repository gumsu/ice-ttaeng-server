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

@RequiredArgsConstructor
@Slf4j
@Controller
public class MoodCheckInGuestController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final MoodCheckInService moodCheckInService;

    // TODO 메시지를 보냈던 사람한테만 다시 리턴해야함
    @MessageMapping("/moodcheckin/{roomName}/submit-name")
    @SendTo("/topic/moodcheckin/{roomName}/submit-name")
    public ActivityRoomMessage joinRoom(@DestinationVariable(value = "roomName") String roomName, StompHeaderAccessor stompHeaderAccessor, ActivityRoomMessage activityRoomMessage) {
        log.info("룸네임 " + roomName);
        log.info("메시지 " + activityRoomMessage);
        // TODO 어떠한 룸인지도 저장이 필요 (이름만 저장한 상태)
        moodCheckInService.registerName((String) activityRoomMessage.payload(), stompHeaderAccessor.getSessionId());
        simpMessageSendingOperations.convertAndSend("/topic/moodcheckin/"+roomName+"/number", moodCheckInService.getNumber());
        return new ActivityRoomMessage("타입", activityRoomMessage.payload() + "님의 이름을 등록하였습니다.", null);
    }

    @MessageMapping("/moodcheckin/{roomName}/submit-mood")
    @SendTo("/topic/moodcheckin/{roomName}/submit-mood")
    public ActivityRoomMessage submitMoodCheckIn(@DestinationVariable(value = "roomName") String roomName, StompHeaderAccessor stompHeaderAccessor, ActivityRoomMessage activityRoomMessage) {
        log.info("룸네임 " + roomName);
        log.info("메시지 " + activityRoomMessage);
        moodCheckInService.updateMood((String) activityRoomMessage.payload(), stompHeaderAccessor.getSessionId());
        return new ActivityRoomMessage("타입", "기분 점수를 등록하였습니다.", null);
    }
}
