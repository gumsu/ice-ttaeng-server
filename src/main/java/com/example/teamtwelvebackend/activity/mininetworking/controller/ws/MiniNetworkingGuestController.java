package com.example.teamtwelvebackend.activity.mininetworking.controller.ws;

import com.example.teamtwelvebackend.activity.mininetworking.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.activity.mininetworking.service.MiniNetworkingService;
import com.example.teamtwelvebackend.ws.RoomInfoMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MiniNetworkingGuestController {
    private final MiniNetworkingService miniNetworkingService;

    @MessageMapping("/mininetworking/{roomName}/get-info")
    @SendToUser("/queue/reply")
    public ActivityRoomMessage getRoomInfo(@DestinationVariable String roomName) {
        RoomInfoMessage room = miniNetworkingService.getRoomInfoByName(roomName);
        return new ActivityRoomMessage("ROOM_INFO", "", room);
    }
}