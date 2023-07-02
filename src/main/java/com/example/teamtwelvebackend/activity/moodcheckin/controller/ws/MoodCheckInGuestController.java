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

    @MessageMapping("/moodcheckin/{roomName}/submit-name")
    @SendTo("/queue/reply")
    public ActivityRoomMessage joinRoom(@DestinationVariable(value = "roomName") String roomName, StompHeaderAccessor stompHeaderAccessor, String userName) {
        moodCheckInService.registerName(roomName, userName, stompHeaderAccessor.getSessionId());
        simpMessageSendingOperations.convertAndSend("/topic/moodcheckin/"+roomName+"/guest-number", moodCheckInService.getGuestNumber(roomName));
        return new ActivityRoomMessage("ANSWER_SUBMITTED", userName + "님의 이름을 등록하였습니다.", "{}");
    }

    @MessageMapping("/moodcheckin/{roomName}/submit-mood")
    @SendTo("/queue/reply")
    public ActivityRoomMessage submitMoodCheckIn(@DestinationVariable(value = "roomName") String roomName, StompHeaderAccessor stompHeaderAccessor, String mood) {
        moodCheckInService.updateMood(mood, stompHeaderAccessor.getSessionId());
        simpMessageSendingOperations.convertAndSend("/topic/moodcheckin/"+roomName+"/submit-number", moodCheckInService.getSubmitNumber(roomName));
        return new ActivityRoomMessage("ANSWER_SUBMITTED", "기분을 등록하였습니다.", "{}");
    }
}
