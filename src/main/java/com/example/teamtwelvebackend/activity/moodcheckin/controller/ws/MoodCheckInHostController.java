package com.example.teamtwelvebackend.activity.moodcheckin.controller.ws;

import com.example.teamtwelvebackend.activity.moodcheckin.service.MoodCheckInService;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MoodCheckInHostController {

    private final MoodCheckInService moodCheckInService;

    @MessageMapping("/moodcheckin/{roomName}/start")
    @SendTo("/topic/moodcheckin/{roomName}")
    public ActivityRoomMessage start(@DestinationVariable(value = "roomName") String roomName) {
        return moodCheckInService.proceed(roomName);
    }

    @MessageMapping("/moodcheckin/{roomName}/random")
    @SendTo("/topic/moodcheckin/{roomName}")
    public ActivityRoomMessage random(@DestinationVariable(value = "roomName") String roomName) {
        return moodCheckInService.random(roomName);
    }
}
