package com.example.teamtwelvebackend.activity.speedgame.service;

import com.example.teamtwelvebackend.activity.speedgame.domain.Room;
import com.example.teamtwelvebackend.activity.speedgame.domain.UserAnswer;
import com.example.teamtwelvebackend.activity.speedgame.repository.RoomRepository;
import com.example.teamtwelvebackend.activity.speedgame.repository.UserAnswerRepository;
import com.example.teamtwelvebackend.activity.speedgame.service.dto.RoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GuestService {
    final RoomRepository roomRepository;
    final UserAnswerRepository userAnswerRepository;

    /**
     * 참가자가 정답을 제출하는 기능
     *
     * @param roomName 참가하고 있는 방 이름
     * @param userId 참가자 id? name?
     * @param questionId 문제 id
     * @param answerId 정답 id
     */
    public void submitAnswer(String roomName, String userId, String questionId, String answerId) {
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
        Room gameRoom = roomRepository.findByName(roomName).orElseThrow();
        return new RoomDto(gameRoom.getName(), gameRoom.getName());
    }
}
