package com.example.teamtwelvebackend.activity.speedgame.controller.ws.message;

import java.util.List;

public record SpeedGameQuestionMessage (
    String questionId,
    Integer order,
    String questionText,
    List<Answer> answers


) {
    public record Answer (
        Long answerId,
        Integer order,
        String answerText
    ) {}
}
