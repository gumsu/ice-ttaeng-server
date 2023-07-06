package com.example.teamtwelvebackend.activity.moodcheckin.controller.ws;

import com.example.teamtwelvebackend.activity.moodcheckin.controller.ws.message.SubmitMood;
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

    @MessageMapping("/moodcheckin/{roomName}/submit-mood")
    @SendTo("/queue/reply")
    public ActivityRoomMessage submitMoodCheckIn(@DestinationVariable(value = "roomName") String roomName, StompHeaderAccessor stompHeaderAccessor, SubmitMood submitMood) {
        moodCheckInService.updateMood(submitMood.mood(), stompHeaderAccessor.getSessionId());
        simpMessageSendingOperations.convertAndSend("/topic/moodcheckin/"+roomName, moodCheckInService.getSubmitNumber(roomName));
        return new ActivityRoomMessage("ANSWER_SUBMITTED", "기분을 등록하였습니다.", "{}");
    }
}
