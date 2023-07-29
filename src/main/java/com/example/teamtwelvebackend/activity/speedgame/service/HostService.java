package com.example.teamtwelvebackend.activity.speedgame.service;

import com.example.teamtwelvebackend.activity.speedgame.controller.rest.request.SpeedGameCreateRequest;
import com.example.teamtwelvebackend.ws.Participant;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.AnswerMessage;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.QuestionMessage;
import com.example.teamtwelvebackend.activity.speedgame.domain.*;
import com.example.teamtwelvebackend.activity.speedgame.repository.*;
import com.example.teamtwelvebackend.activity.speedgame.service.dto.RoomCreatedDto;
import com.example.teamtwelvebackend.ws.ParticipantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class HostService {
    final RoomRepository roomRepository;
    final QuestionRepository questionRepository;
    final AnswerRepository answerRepository;

    final UserAnswerRepository userAnswerRepository;
    final UserNicknameRepository userNicknameRepository;

    final ParticipantService participantService;


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
            return new Question(speedGameRoom.getName(), question.getNumber(), questionText, answers);
        }).toList();
        questionRepository.saveAll(questionList);

        return new RoomCreatedDto(speedGameRoom.getName(), speedGameRoom.getName());
    }

    /**
     * 다음 상태로 진행
     *
     * @param creatorId
     * @param roomName  다음 상태로 진행 시킬 방 이름
     * @return 다음 상태에 대한 메시지
     */
    @Transactional
    public ActivityRoomMessage proceed(String creatorId, String roomName) {
        SpeedGameRoom speedGameRoom = roomRepository.findByName(roomName).orElseThrow();
        if (!speedGameRoom.getCreatedBy().equals(creatorId)) {
            throw new IllegalStateException("방을 만든 사람이 아닙니다.");
        }
        RoomStatus status = speedGameRoom.next();
        switch (status) {
            case CREATED_ROOM -> {
                throw new IllegalStateException("초기 상태로 돌아올 수 없음");
            }
            case OPENED_QUESTION -> {
                Question question = questionRepository.findByRoomNameAndNumber(roomName, speedGameRoom.getCurrentQuestion())
                        .orElseThrow();
                String questionText = question.getQuestionText();
                List<QuestionMessage.Answer> answers = question.getAnswers().stream()
                        .map(answer -> new QuestionMessage.Answer(answer.getId(), answer.getNumber(), answer.getAnswerText())).toList();

                QuestionMessage message = new QuestionMessage(question.getId(), speedGameRoom.getCurrentQuestion(), speedGameRoom.getTotalQuestion(), questionText, answers);
                return new ActivityRoomMessage(status.toString(), "", message);
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

    private AnswerMessage getAnswer(String roomName, int number) {
        Question question = questionRepository.findByRoomNameAndNumber(roomName, number)
                .orElseThrow();
        List<Answer> answer = question.getCorrectAnswer();
        List<Long> answerIds = answer.stream().map(Answer::getId).toList();
        List<String> correctAnswerText = answer.stream().map(Answer::getAnswerText).toList();
        log.info("roomName: {} questionId: {} answerId: {}", roomName, question.getId(), answerIds);

        List<String> userIdList = userAnswerRepository.findByRoomNameAndQuestionIdAndAnswerIdIn(roomName, question.getId(), answerIds).stream().map(UserAnswer::getUserId).toList();
        userIdList.forEach(userId -> log.info("correct answer userId: +"+userId));

        String simpDestination = "/topic/speedgame/"+roomName;
        List<Participant> participant1 = participantService.getParticipant(simpDestination);

        participant1.forEach(p -> log.info("participant: +"+p.getName() + "/"+p.getNickname()));
        List<String> users = participant1.stream()
                .filter(participant -> userIdList.contains(participant.getName()))
                .map(participant -> participant.getNickname())
                .toList();
        return new AnswerMessage(correctAnswerText, users);
    }
}
