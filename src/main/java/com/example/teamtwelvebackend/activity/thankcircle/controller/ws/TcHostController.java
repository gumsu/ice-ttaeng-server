package com.example.teamtwelvebackend.activity.thankcircle.controller.ws;

import com.example.teamtwelvebackend.CustomJwtAuthenticationToken;
import com.example.teamtwelvebackend.activity.thankcircle.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.activity.thankcircle.service.TcHostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TcHostController {
    final TcHostService service;

    /**
     * 감사 서클 시작 또는 다음
     * <p>
     * 호스트 전용
     *
     * 개설된 방에서 감사 서클을 시작
     *
     * 개설된 방이 없으면 시작 불가
     * 이미 게임이 시작된 이후 상태면 시작 불가
     * @return
     */
    @MessageMapping("/thankcircle/{roomName}/start")
    @SendTo("/topic/thankcircle/{roomName}")
    public ActivityRoomMessage start(CustomJwtAuthenticationToken principal, @DestinationVariable String roomName) {
        String creatorId = principal.getName();
        return service.proceed(creatorId, roomName);
    }

    /**
     * 감사 서클 종료
     * <p>
     * 호스트 전용
     *
     * @return
     */
    @MessageMapping("/thankcircle/{roomName}/close")
    @SendTo("/topic/thankcircle/{roomName}")
    public ActivityRoomMessage close(CustomJwtAuthenticationToken principal, @DestinationVariable String roomName) {
        String creatorId = principal.getName();
        return service.close(creatorId, roomName);
    }


}