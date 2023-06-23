package com.example.teamtwelvebackend.activity.speedgame.controller.ws;

import com.example.teamtwelvebackend.activity.speedgame.service.HostService;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SpeedGameHostController {
    final HostService service;

    /**
     * 스피드게임 시작 또는 다음
     * <p>
     * 호스트 전용
     *
     * 개설된 방에서 스피드게임을 시작
     *
     * 개설된 방이 없으면 시작 불가
     * 이미 게임이 시작된 이후 상태면 시작 불가
     * @return
     */
    @MessageMapping("/speedgame/{roomName}/start")
    @SendTo("/topic/speedgame/{roomName}")
    public ActivityRoomMessage start(@DestinationVariable String roomName) {
        return service.proceed(roomName);
    }

}