package com.example.teamtwelvebackend.ws.speedgame;

import com.example.teamtwelvebackend.activity.service.SpeedGameService;
import com.example.teamtwelvebackend.ws.speedgame.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.ws.speedgame.message.SpeedGameSubmitAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class SpeedGameGuestController {
    final SpeedGameService speedGameService;

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
    @SendTo("/topic/speedgame/{roomName}")
    public ActivityRoomMessage submitAnswer(@DestinationVariable String roomName,
                                            SpeedGameSubmitAnswer answer) {
        speedGameService.submitAnswer(roomName, answer.userId(), answer.questionId(), answer.answerId());
        return new ActivityRoomMessage("ANSWER_SUBMITTED", "정답이 제출되었습니다", "{}");
    }

}