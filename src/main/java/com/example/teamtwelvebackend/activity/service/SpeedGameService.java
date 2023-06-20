package com.example.teamtwelvebackend.activity.service;

import com.example.teamtwelvebackend.activity.controller.request.SpeedGameCreateRequest;
import com.example.teamtwelvebackend.activity.domain.SpeedGameAnswerEntity;
import com.example.teamtwelvebackend.activity.domain.SpeedGameQuestion;
import com.example.teamtwelvebackend.activity.domain.SpeedGameRoom;
import com.example.teamtwelvebackend.activity.domain.SpeedGameRoomStatus;
import com.example.teamtwelvebackend.activity.repository.SpeedGameAnswerRepository;
import com.example.teamtwelvebackend.activity.repository.SpeedGameQuestionRepository;
import com.example.teamtwelvebackend.activity.repository.SpeedGameRoomRepository;
import com.example.teamtwelvebackend.ws.speedgame.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.ws.speedgame.message.SpeedGameAnswerMessage;
import com.example.teamtwelvebackend.ws.speedgame.message.SpeedGameQuestionMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SpeedGameService {
    final ObjectMapper objectMapper;
    final SpeedGameRoomRepository speedGameRoomRepository;
    final SpeedGameAnswerRepository speedGameAnswerRepository;
    final SpeedGameQuestionRepository speedGameQuestionRepository;

    SpeedGameRoom speedGameRoom;

    @PostConstruct
    public void init() {
        speedGameRoom = new SpeedGameRoom("sample");
    }

    public ActivityRoomMessage getContent(String roomName) {
        SpeedGameRoomStatus status = speedGameRoom.next();
        switch (status) {
            case CREATED_ROOM -> {
            }
            case OPENED_QUESTION -> {
                // 메시지에 문제를 실어 보내야함
                SpeedGameQuestionMessage question = getQuestion(roomName, speedGameRoom.getCurrentQuestion());
                return new ActivityRoomMessage(status.toString(), "", question);
            }
            case OPENED_ANSWER -> {
                // 메시지에 정답과 정답자를 실어 보내야 함
                SpeedGameAnswerMessage answer = getAnswer(roomName, speedGameRoom.getCurrentQuestion());
                getSubmitAnswer(roomName, speedGameRoom.getCurrentQuestion());
                return new ActivityRoomMessage(status.toString(), "", answer);
            }
            case CLOSED_ROOM -> {
                return new ActivityRoomMessage(status.toString(), "", "{}");
            }
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
        return null;
    }

    private void getSubmitAnswer(String roomName, Integer currentQuestion) {
        List<SpeedGameAnswerEntity> submittedAnswers = speedGameAnswerRepository.getSubmittedAnswers(roomName, currentQuestion);
        submittedAnswers.forEach(entity -> System.out.println(entity.getAnswerId()));
    }

    public SpeedGameQuestionMessage getQuestion(String roomName, int number) {

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
            return objectMapper.readValue(sample1, SpeedGameQuestionMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public SpeedGameAnswerMessage getAnswer(String roomName, int number) {
        return new SpeedGameAnswerMessage("대답 " + number, List.of("참가자 1"));
    }

    public void submitAnswer(String roomName, String userId, String questionId, String answerId) {
        speedGameAnswerRepository.submitAnswer(roomName, userId, questionId, answerId);
    }

    @Transactional
    public SpeedGameCreatedDto createRoom(String creatorId, SpeedGameCreateRequest request) {
        SpeedGameRoom speedGameRoom1 = speedGameRoomRepository.save(new SpeedGameRoom(creatorId));

        List<SpeedGameCreateRequest.SpeedGameQuestion> questions = request.getQuestions();
        List<SpeedGameQuestion> questionList = questions.stream().map(question -> {
            String questionText = question.getQuestionText();
            List<SpeedGameCreateRequest.SpeedGameQuestion.Answer> answers = question.getAnswers();
            return new SpeedGameQuestion(questionText, answers);
        }).toList();
        speedGameQuestionRepository.saveAll(questionList);

        return new SpeedGameCreatedDto(speedGameRoom1.getName(), speedGameRoom1.getName());
    }

    public SpeedGameRoomDto getRoomByName(String roomName) {
        SpeedGameRoom gameRoom = speedGameRoomRepository.findByName(roomName).orElseThrow();
        return new SpeedGameRoomDto(gameRoom.getName(), gameRoom.getName());
    }
}
