package com.example.teamtwelvebackend.activity.speedgame.controller.ws;

import com.example.teamtwelvebackend.activity.speedgame.service.GuestService;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.SubmitAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class SpeedGameGuestController {
    final GuestService guestService;

    // 참가자 입장에서 입장을 MessageMapping 하는 것이 필요한가?
    // 토픽을 구독하는것으로 입장이 되는것인가?
    // 입장 코드 검사 로직이 필요하다. 아무 코드나 입력한다고 해서 입장이 되는것이 아님 -> 프론트에서?


    /**
     * 스피드게임 정답 제출
     * <p>
     * 참가자 정답 제출
     * <p>
     *
     * @return
     */
    @MessageMapping("/speedgame/{roomName}/submit-answer")
    @SendToUser("/queue/reply")
    public ActivityRoomMessage submitAnswer(@DestinationVariable String roomName,
                                            Principal principal,
                                            SubmitAnswer answer) {
        guestService.submitAnswer(roomName, principal.getName(), answer.questionId(), answer.answerId());
        return new ActivityRoomMessage("ANSWER_SUBMITTED", "정답이 제출되었습니다", "{}");
    }
}