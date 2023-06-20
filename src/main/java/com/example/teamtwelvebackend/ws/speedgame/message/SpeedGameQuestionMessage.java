package com.example.teamtwelvebackend.ws.speedgame.message;

import lombok.Getter;

import java.util.List;

@Getter
public class SpeedGameQuestionMessage {
    String questionId;
    Integer order;
    String questionText;
    List<Answer> answers;

    @Getter
    private static class Answer {
        Integer answerId;
        Integer order;
        String answerText;
    }
}
