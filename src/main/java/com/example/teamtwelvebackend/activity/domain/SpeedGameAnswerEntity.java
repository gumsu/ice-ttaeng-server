package com.example.teamtwelvebackend.activity.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SpeedGameAnswerEntity {
    String roomName;
    String userId;
    String questionId;
    String answerId;
}
