package com.example.teamtwelvebackend.activity.speedgame.controller.rest.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class SpeedGameCreateRequest {
    @Valid
    List<SpeedGameQuestion> questions;

    @Getter
    public static class SpeedGameQuestion {
        @NotNull
        @Min(1)
        Integer number;
        @NotBlank
        String questionText;
        @Valid
        List<Answer> answers;

        @Getter
        public static class Answer {
            @NotNull
            @Min(1)
            Integer number;
            @NotBlank
            String answerText;
            @NotNull
            Boolean correctAnswer;
        }
    }
}
