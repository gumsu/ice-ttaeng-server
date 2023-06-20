package com.example.teamtwelvebackend.activity.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SpeedGameAnswer {
    @Id
    @GeneratedValue
    Long id;
    Integer answerOrder;
    String answerText;
    Boolean correctAnswer;

    @ManyToOne
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    SpeedGameQuestion question;

    public SpeedGameAnswer(Integer order, String answerText, Boolean correctAnswer, SpeedGameQuestion question) {
        this.answerOrder = order;
        this.answerText = answerText;
        this.correctAnswer = correctAnswer;
        this.question = question;
    }
}
