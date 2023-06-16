package com.example.teamtwelvebackend.ws.speedgame.message;

import lombok.Getter;

import java.util.List;

@Getter
public class SpeedGameQuestion {
    Integer order;
    String questionText;
    List<Answer> answers;

    @Getter
    private static class Answer {
        Integer order;
        String answerText;
    }
}
