package com.example.teamtwelvebackend.activity.speedgame.controller.ws.message;

import java.util.List;

public record QuestionMessage(
    Long questionId,
    Integer number,
    String questionText,
    List<Answer> answers


) {
    public record Answer (
        Long answerId,
        Integer order,
        String answerText
    ) {}
}
