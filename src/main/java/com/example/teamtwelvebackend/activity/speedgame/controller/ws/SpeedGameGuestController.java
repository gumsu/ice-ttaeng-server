package com.example.teamtwelvebackend.activity.speedgame.controller.ws;

import com.example.teamtwelvebackend.activity.speedgame.service.GuestService;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.SpeedGameSubmitAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SpeedGameGuestController {
    final GuestService guestService;

    // 참가자 입장에서 입장을 MessageMapping 하는 것이 필요한가?
    // 토픽을 구독하는것으로 입장이 되는것인가?
    // 입장 코드 검사 로직이 필요하다. 아무 코드나 입력한다고 해서 입장이 되는것이 아님 -> 프론트에서?

    /**
     * 참가자 이름 입력
     *
     * @param roomName
     * @param headerAccessor
     * @param username
     * @return
     */
    @MessageMapping("/speedgame/{roomName}/register-name")
    @SendToUser("/queue/reply")
    public ActivityRoomMessage registerName(@DestinationVariable String roomName,
                                            SimpMessageHeaderAccessor headerAccessor,
                                            String username) {
        String sessionId = headerAccessor.getSessionId();
        System.out.println(sessionId);
        System.out.println(username);
        return new ActivityRoomMessage("NAME_REGISTERED", "이름이 등록되었습니다", "{}");
    }

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
                                            SpeedGameSubmitAnswer answer) {
        guestService.submitAnswer(roomName, answer.userId(), answer.questionId(), answer.answerId());
        return new ActivityRoomMessage("ANSWER_SUBMITTED", "정답이 제출되었습니다", "{}");
    }
}