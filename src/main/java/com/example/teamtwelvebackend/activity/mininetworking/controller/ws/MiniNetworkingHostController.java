package com.example.teamtwelvebackend.activity.mininetworking.controller.ws;

import com.example.teamtwelvebackend.activity.mininetworking.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.activity.mininetworking.controller.ws.message.SubmitGroupingSize;
import com.example.teamtwelvebackend.activity.mininetworking.service.MiniNetworkingService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class MiniNetworkingHostController {

    private final MiniNetworkingService miniNetworkingService;

    @MessageMapping("/mininetworking/{roomName}/start")
    @SendTo("/topic/mininetworking/{roomName}")
    public ActivityRoomMessage start(@DestinationVariable(value = "roomName") String roomName, Principal principal) {
        return miniNetworkingService.proceed(roomName, principal.getName());
    }

    @MessageMapping("/mininetworking/{roomName}/submit-grouping")
    @SendTo("/topic/mininetworking/{roomName}")
    public ActivityRoomMessage submitGroupingNumber(@DestinationVariable(value = "roomName") String roomName, SubmitGroupingSize number) {
        miniNetworkingService.createParticipantGroups(roomName, number);
        return new ActivityRoomMessage("ANSWER_SUBMITTED", "그룹 구성을 완료하였습니다.", "{}");
    }
}
