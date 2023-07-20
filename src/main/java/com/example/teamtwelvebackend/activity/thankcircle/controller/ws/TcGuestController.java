package com.example.teamtwelvebackend.activity.thankcircle.controller.ws;

import com.example.teamtwelvebackend.activity.thankcircle.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.activity.thankcircle.controller.ws.message.RoomInfoMessage;
import com.example.teamtwelvebackend.activity.thankcircle.service.TcGuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TcGuestController {
    final TcGuestService tcGuestService;

    @MessageMapping("/thankcircle/{roomName}/get-info")
    @SendToUser("/queue/reply")
    public ActivityRoomMessage getRoomInfo(@DestinationVariable String roomName) {
        RoomInfoMessage room = tcGuestService.getRoomInfoByName(roomName);
        return new ActivityRoomMessage("ROOM_INFO", "", room);
    }
}