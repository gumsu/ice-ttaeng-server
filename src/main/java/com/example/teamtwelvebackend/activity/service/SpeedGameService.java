package com.example.teamtwelvebackend.activity.service;

import com.example.teamtwelvebackend.activity.domain.SpeedGameRoom;
import com.example.teamtwelvebackend.activity.domain.SpeedGameRoomStatus;
import com.example.teamtwelvebackend.ws.speedgame.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.ws.speedgame.message.SpeedGameAnswer;
import com.example.teamtwelvebackend.ws.speedgame.message.SpeedGameQuestion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SpeedGameService {
    final ObjectMapper objectMapper;

    SpeedGameRoom speedGameRoom;

    @PostConstruct
    public void init() {
        speedGameRoom = new SpeedGameRoom();
    }

    public ActivityRoomMessage getContent(String roomName) {
        speedGameRoom.next();
        SpeedGameRoomStatus status = speedGameRoom.getStatus();
        switch (status) {
            case CREATED_ROOM -> {
            }
            case OPENED_QUESTION -> {
                // 메시지에 문제를 실어 보내야함
                SpeedGameQuestion question = getQuestion(roomName, speedGameRoom.getCurrentQuestion());
                return new ActivityRoomMessage(status.toString(), "", question);
            }
            case OPENED_ANSWER -> {
                // 메시지에 정답과 정답자를 실어 보내야 함
                SpeedGameAnswer answer = getAnswer(roomName, speedGameRoom.getCurrentQuestion());
                return new ActivityRoomMessage(status.toString(), "", answer);
            }
            case CLOSED_ROOM -> {
            }
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
        return null;

    }

    public SpeedGameQuestion getQuestion(String roomName, int number) {

        try {
            String sample1 =  String.format("""
                {
                    "order": 1,
                    "question_text": "질문 내용 %d",
                    "answers": [
                        {
                            "order": 1,
                            "answer_text": "대답 1"
                        },
                        {
                            "order": 2,
                            "answer_text": "대답 2"
                        },
                        {
                            "order": 3,
                            "answer_text": "대답 3"
                        }
                    ]
                }
                """, number);
            return objectMapper.readValue(sample1, SpeedGameQuestion.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public SpeedGameAnswer getAnswer(String roomName, int number) {
        return new SpeedGameAnswer("대답 "+number, List.of("참가자 1"));
    }
}
