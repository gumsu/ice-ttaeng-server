package com.example.teamtwelvebackend.activity.controller.request;

import lombok.Getter;

import java.util.List;

@Getter
public class SpeedGameCreateRequest {
    List<SpeedGameQuestion> questions;

    @Getter
    public static class SpeedGameQuestion {
        Integer order;
        String questionText;
        List<Answer> answers;

        private static class Answer {
            Integer order;
            String answerText;
            Boolean correctAnswer;
        }
    }
}
