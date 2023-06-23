package com.example.teamtwelvebackend.activity.speedgame.service;

import com.example.teamtwelvebackend.activity.speedgame.controller.rest.request.SpeedGameCreateRequest;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.AnswerMessage;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.QuestionMessage;
import com.example.teamtwelvebackend.activity.speedgame.domain.*;
import com.example.teamtwelvebackend.activity.speedgame.repository.*;
import com.example.teamtwelvebackend.activity.speedgame.service.dto.RoomCreatedDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HostService {
    final RoomRepository roomRepository;
    final QuestionRepository questionRepository;
    final AnswerRepository answerRepository;

    final UserAnswerRepository userAnswerRepository;
    final UserNicknameRepository userNicknameRepository;


    /**
     * 주최자가 방을 만드는 기능
     *
     * @param creatorId 주최자 id (UUID)
     * @param request 문제 정보
     * @return
     */
    @Transactional
    public RoomCreatedDto createRoom(String creatorId, SpeedGameCreateRequest request) {

        List<SpeedGameCreateRequest.SpeedGameQuestion> questions = request.getQuestions();

        SpeedGameRoom speedGameRoom = roomRepository.save(new SpeedGameRoom(creatorId, questions.size()));

        List<Question> questionList = questions.stream().map(question -> {
            String questionText = question.getQuestionText();
            List<SpeedGameCreateRequest.SpeedGameQuestion.Answer> answers = question.getAnswers();
            return new Question(speedGameRoom.getName(), question.getOrder(), questionText, answers);
        }).toList();
        questionRepository.saveAll(questionList);

        return new RoomCreatedDto(speedGameRoom.getName(), speedGameRoom.getName());
    }

    /**
     * 다음 상태로 진행
     *
     * @param roomName 다음 상태로 진행 시킬 방 이름
     * @return 다음 상태에 대한 메시지
     */
    @Transactional
    public ActivityRoomMessage proceed(String roomName) {
        SpeedGameRoom speedGameRoom = roomRepository.findByName(roomName).orElseThrow();
        RoomStatus status = speedGameRoom.next();
        switch (status) {
            case CREATED_ROOM -> {
                throw new IllegalStateException("초기 상태로 돌아올 수 없음");
            }
            case OPENED_QUESTION -> {
                // 메시지에 문제를 실어 보내야함
                QuestionMessage question = getQuestion(roomName, speedGameRoom.getCurrentQuestion());
                return new ActivityRoomMessage(status.toString(), "", question);
            }
            case OPENED_ANSWER -> {
                // 메시지에 정답과 정답자를 실어 보내야 함
                AnswerMessage answer = getAnswer(roomName, speedGameRoom.getCurrentQuestion());
                return new ActivityRoomMessage(status.toString(), "", answer);
            }
            case CLOSED_ROOM -> {
                return new ActivityRoomMessage(status.toString(), "", "{}");
            }
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    private QuestionMessage getQuestion(String roomName, int number) {
        Question question = questionRepository.findByRoomNameAndNumber(roomName, number)
                .orElseThrow();
        String questionText = question.getQuestionText();
        List<QuestionMessage.Answer> answers = question.getAnswers().stream()
                .map(answer -> new QuestionMessage.Answer(answer.getId(), answer.getNumber(), answer.getAnswerText())).toList();
        return new QuestionMessage(question.getId(), number, questionText, answers);
    }

    private AnswerMessage getAnswer(String roomName, int number) {
        Question question = questionRepository.findByRoomNameAndNumber(roomName, number)
                .orElseThrow();
        Answer answer = answerRepository.findById(question.getId()).orElseThrow();
        List<String> correctAnswerText = question.getCorrectAnswer().stream().map(Answer::getAnswerText).toList();

        List<String> userIdList = userAnswerRepository.findByRoomNameAndQuestionIdAndAnswerId(roomName, question.getId(), answer.getId()).stream().map(UserAnswer::getUserId).toList();
        List<String> users = userNicknameRepository.findByRoomNameAndSessionIdIn(roomName, userIdList)
                .stream().map(UserNickname::getNickname).toList();
        return new AnswerMessage(correctAnswerText, users);
    }
}
