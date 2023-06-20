package com.example.teamtwelvebackend.activity.domain;

import com.example.teamtwelvebackend.activity.controller.request.SpeedGameCreateRequest;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class SpeedGameQuestion {
    @Id
    @GeneratedValue
    Long id;

    String questionText;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_id")
    List<SpeedGameAnswer> answers;

    public SpeedGameQuestion(String questionText, List<SpeedGameCreateRequest.SpeedGameQuestion.Answer> answers) {
        this.questionText = questionText;
        this.answers = answers.stream()
                .map(answer -> new SpeedGameAnswer(answer.getOrder(), answer.getAnswerText(), answer.getCorrectAnswer(), this))
                .toList();
    }
}
