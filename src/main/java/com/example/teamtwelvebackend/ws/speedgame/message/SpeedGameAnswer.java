package com.example.teamtwelvebackend.ws.speedgame.message;

import lombok.Getter;

import java.util.List;

@Getter
public class SpeedGameAnswer {
    String answerText;
    List<String> correctPersons;

    public SpeedGameAnswer(String answerText, List<String> correctPersons) {
        this.answerText = answerText;
        this.correctPersons = correctPersons;
    }
}
