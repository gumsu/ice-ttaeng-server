package com.example.teamtwelvebackend.activity.speedgame.service;

import com.example.teamtwelvebackend.activity.speedgame.domain.UserNickname;
import com.example.teamtwelvebackend.activity.speedgame.domain.SpeedGameRoom;
import com.example.teamtwelvebackend.activity.speedgame.domain.UserAnswer;
import com.example.teamtwelvebackend.activity.speedgame.repository.UserNicknameRepository;
import com.example.teamtwelvebackend.activity.speedgame.repository.RoomRepository;
import com.example.teamtwelvebackend.activity.speedgame.repository.UserAnswerRepository;
import com.example.teamtwelvebackend.activity.speedgame.service.dto.RoomDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GuestService {
    final RoomRepository roomRepository;
    final UserAnswerRepository userAnswerRepository;
    final UserNicknameRepository userNicknameRepository;

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
        SpeedGameRoom gameSpeedGameRoom = roomRepository.findByName(roomName).orElseThrow();
        return new RoomDto(gameSpeedGameRoom.getName(), gameSpeedGameRoom.getName());
    }

    /**
     * 참가자가 입력한 닉네임과 웹소켓 세션 id 를 연결
     *
     * @param roomName 방 아이디
     * @param sessionId 참가자 웹소켓 세션 id
     * @param username 참가자가 입력한 닉네임
     */
    @Transactional
    public void registerNickname(String roomName, String sessionId, String username) {
        userNicknameRepository.save(new UserNickname(roomName, sessionId, username));
    }
}
