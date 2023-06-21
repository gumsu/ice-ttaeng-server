package com.example.teamtwelvebackend.activity.speedgame.controller.ws.message;

import lombok.Getter;

import java.util.List;

@Getter
public class SpeedGameAnswerMessage {
    String answerText;
    List<String> correctPersons;

    public SpeedGameAnswerMessage(String answerText, List<String> correctPersons) {
        this.answerText = answerText;
        this.correctPersons = correctPersons;
    }
}
