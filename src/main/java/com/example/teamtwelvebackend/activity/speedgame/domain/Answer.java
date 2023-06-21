package com.example.teamtwelvebackend.activity.speedgame.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "sg_answer")
public class Answer {
    @Id
    @GeneratedValue
    Long id;
    String roomName;
    Integer number;
    String answerText;
    Boolean correctAnswer;

    @ManyToOne
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    Question question;

    public Answer(String roomName, Integer order, String answerText, Boolean correctAnswer, Question question) {
        this.roomName = roomName;
        this.number = order;
        this.answerText = answerText;
        this.correctAnswer = correctAnswer;
        this.question = question;
    }
}
