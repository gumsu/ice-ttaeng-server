package com.example.teamtwelvebackend.activity.speedgame.controller.ws.message;

import java.util.List;

public record AnswerMessage(
        List<String> answerText,
        List<String> correctUserList
) {
}
