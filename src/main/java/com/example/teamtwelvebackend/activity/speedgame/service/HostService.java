package com.example.teamtwelvebackend.activity.speedgame.service;

import com.example.teamtwelvebackend.activity.speedgame.controller.rest.request.SpeedGameCreateRequest;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.SpeedGameAnswerMessage;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.SpeedGameQuestionMessage;
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
    final AnswerRepository answerJpaRepository;

    final UserAnswerRepository userAnswerRepository;

    /**
     * 다음 상태로 진행
     *
     * @param roomName 다음 상태로 진행 시킬 방 이름
     * @return 다음 상태에 대한 메시지
     */
    @Transactional
    public ActivityRoomMessage process(String roomName) {
        Room room = roomRepository.findByName(roomName).orElseThrow();
        RoomStatus status = room.next();
        switch (status) {
            case CREATED_ROOM -> {
                throw new IllegalStateException("초기 상태로 돌아올 수 없음");
            }
            case OPENED_QUESTION -> {
                // 메시지에 문제를 실어 보내야함
                SpeedGameQuestionMessage question = getQuestion(roomName, room.getCurrentQuestion());
                return new ActivityRoomMessage(status.toString(), "", question);
            }
            case OPENED_ANSWER -> {
                // 메시지에 정답과 정답자를 실어 보내야 함
                SpeedGameAnswerMessage answer = getAnswer(roomName, room.getCurrentQuestion());
                String correctAnswerId = ""; // TODO
                getSubmitAnswer(roomName, room.getCurrentQuestion(), correctAnswerId);
                return new ActivityRoomMessage(status.toString(), "", answer);
            }
            case CLOSED_ROOM -> {
                return new ActivityRoomMessage(status.toString(), "", "{}");
            }
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    private void getSubmitAnswer(String roomName, Integer currentQuestion, String answerId) {
        List<UserAnswer> submittedAnswers = userAnswerRepository.findByRoomNameAndQuestionIdAndAnswerId(roomName, String.valueOf(currentQuestion), answerId);
        submittedAnswers.forEach(entity -> System.out.println(entity.getAnswerId()));
    }

    private SpeedGameQuestionMessage getQuestion(String roomName, int number) {
        Question question = questionRepository.findByRoomNameAndNumber(roomName, number)
                .orElseThrow();
        Long id = question.getId();
        Integer order = question.getNumber();
        String questionText = question.getQuestionText();
        List<SpeedGameQuestionMessage.Answer> answers = question.getAnswers().stream()
                .map(answer -> new SpeedGameQuestionMessage.Answer(answer.getId(), answer.getNumber(), answer.getAnswerText())).toList();
        return new SpeedGameQuestionMessage(
                String.valueOf(id), order, questionText, answers
        );
    }

    private SpeedGameAnswerMessage getAnswer(String roomName, int number) {
        Question question = questionRepository.findByRoomNameAndNumber(roomName, number)
                .orElseThrow();

        Answer answer = answerJpaRepository.findByRoomNameAndNumber(roomName, number).orElseThrow();
        return new SpeedGameAnswerMessage(answer.getAnswerText(), List.of("참가자 1")); // TODO 정답자 명단
    }

    /**
     * 주최자가 방을 만드는 기능
     *
     * @param creatorId 주최자 id (UUID)
     * @param request 문제 정보
     * @return
     */
    @Transactional
    public RoomCreatedDto createRoom(String creatorId, SpeedGameCreateRequest request) {
        Room room = roomRepository.save(new Room(creatorId));

        List<SpeedGameCreateRequest.SpeedGameQuestion> questions = request.getQuestions();
        List<Question> questionList = questions.stream().map(question -> {
            String questionText = question.getQuestionText();
            List<SpeedGameCreateRequest.SpeedGameQuestion.Answer> answers = question.getAnswers();
            return new Question(room.getName(), question.getOrder(), questionText, answers);
        }).toList();
        questionRepository.saveAll(questionList);

        return new RoomCreatedDto(room.getName(), room.getName());
    }
}
