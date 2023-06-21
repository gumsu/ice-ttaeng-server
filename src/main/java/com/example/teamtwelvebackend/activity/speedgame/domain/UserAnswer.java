package com.example.teamtwelvebackend.activity.speedgame.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "sg_user_answer")
public class UserAnswer {
    @Id
    @GeneratedValue
    Long id;
    String roomName;
    String userId;
    String questionId;
    String answerId;

    public UserAnswer(String roomName, String userId, String questionId, String answerId) {
        this.roomName = roomName;
        this.userId = userId;
        this.questionId = questionId;
        this.answerId = answerId;
    }
}
