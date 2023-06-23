package com.example.teamtwelvebackend.activity.speedgame.domain;

import com.example.teamtwelvebackend.activity.speedgame.controller.rest.request.SpeedGameCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "sg_question")
public class Question {
    @Id
    @GeneratedValue
    Long id;

    String roomName;

    Integer number;

    String questionText;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_id")
    List<Answer> answers;

    public Question(String roomName, Integer number, String questionText, List<SpeedGameCreateRequest.SpeedGameQuestion.Answer> answers) {
        this.roomName = roomName;
        this.number = number;
        this.questionText = questionText;
        this.answers = answers.stream()
                .map(answer -> new Answer(roomName, answer.getOrder(), answer.getAnswerText(), answer.getCorrectAnswer(), this))
                .toList();
    }

    public List<Answer> getCorrectAnswer() {
        return answers.stream().filter(Answer::getCorrectAnswer).toList();
    }
}
