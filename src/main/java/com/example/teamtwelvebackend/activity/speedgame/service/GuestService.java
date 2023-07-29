package com.example.teamtwelvebackend.activity.speedgame.service;

import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.SubmitParticipantMessage;
import com.example.teamtwelvebackend.qr.NaverShortUrlService;
import com.example.teamtwelvebackend.qr.ShortURLAndQrVO;
import com.example.teamtwelvebackend.ws.Participant;
import com.example.teamtwelvebackend.activity.speedgame.domain.SpeedGameRoom;
import com.example.teamtwelvebackend.activity.speedgame.domain.UserAnswer;
import com.example.teamtwelvebackend.activity.speedgame.repository.UserNicknameRepository;
import com.example.teamtwelvebackend.activity.speedgame.repository.RoomRepository;
import com.example.teamtwelvebackend.activity.speedgame.repository.UserAnswerRepository;
import com.example.teamtwelvebackend.activity.speedgame.service.dto.RoomDto;
import com.example.teamtwelvebackend.ws.ParticipantService;
import com.example.teamtwelvebackend.ws.RoomInfoMessage;
import com.example.teamtwelvebackend.ws.RoomParticipantChangedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GuestService {
    public static final String ACTIVITY_TYPE = "speedgame";
    final RoomRepository roomRepository;
    final UserAnswerRepository userAnswerRepository;
    final UserNicknameRepository userNicknameRepository;

    final ParticipantService participantService;
    final NaverShortUrlService naverShortUrlService;
    final SimpMessagingTemplate template;


    @Value("${service-url}")
    private String serviceUrl;

    /**
     * 참가자가 정답을 제출하는 기능
     *
     * @param roomName 참가하고 있는 방 이름
     * @param userId 참가자 웹소켓 session id
     * @param questionId 문제 id
     * @param answerId 정답 id
     */
    public void submitAnswer(String roomName, String userId, Long questionId, Long answerId) {
        UserAnswer entity = new UserAnswer(roomName, userId, questionId, answerId);
        userAnswerRepository.save(entity);

        int count = userAnswerRepository.countByRoomNameAndQuestionId(roomName, answerId);
        SubmitParticipantMessage payload = new SubmitParticipantMessage(count);
        String simpDestination = "/topic/%s/%s".formatted(ACTIVITY_TYPE, roomName);
        template.convertAndSend(simpDestination+"/submit-count", new ActivityRoomMessage("SUBMITTED_PARTICIPANT", "", payload));
    }

    /**
     * 참가 대상자들에게 방 참가 정보를 알려주기 위한 정보 획득 기능
     *
     * 방 참가 정보: 입장 코드, qr code, 접속 링크 등
     *
     * @param roomName 정보를 확인 할 방 이름
     * @return 방 참가 정보
     */
    public RoomDto getRoomDtoByName(String roomName) {
        String simpDestination = "/topic/%s/%s".formatted(ACTIVITY_TYPE, roomName);
        List<Participant> participantList = participantService.getParticipant(simpDestination);

        String roomUrl = "%s/%s/%s".formatted(serviceUrl, ACTIVITY_TYPE, roomName);
        ShortURLAndQrVO shortURLAndQrCode = naverShortUrlService.createShortURLAndQrCode(roomUrl);

        SpeedGameRoom gameSpeedGameRoom = roomRepository.findByName(roomName).orElseThrow();
        return new RoomDto(gameSpeedGameRoom.getName(), gameSpeedGameRoom.getName(), shortURLAndQrCode.getUrl(), shortURLAndQrCode.getQr(), participantList.size());
    }

    public RoomInfoMessage getRoomInfoByName(String roomName) {
        String simpDestination = "/topic/%s/%s".formatted(ACTIVITY_TYPE, roomName);
        List<Participant> participantList = participantService.getParticipant(simpDestination);
        return new RoomInfoMessage(participantList.size());
    }
}
