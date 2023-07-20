package com.example.teamtwelvebackend.activity.thankcircle.service;

import com.example.teamtwelvebackend.activity.thankcircle.controller.ws.message.RoomInfoMessage;
import com.example.teamtwelvebackend.activity.thankcircle.domain.ThankCircleRoom;
import com.example.teamtwelvebackend.activity.thankcircle.repository.TcRoomRepository;
import com.example.teamtwelvebackend.activity.thankcircle.service.dto.RoomDto;
import com.example.teamtwelvebackend.ws.Participant;
import com.example.teamtwelvebackend.ws.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TcGuestService {
    final TcRoomRepository tcRoomRepository;

    final ParticipantService participantService;

    /**
     * 참가 대상자들에게 방 참가 정보를 알려주기 위한 정보 획득 기능
     *
     * 방 참가 정보: 입장 코드, qr code, 접속 링크 등
     *
     * @param roomName 정보를 확인 할 방 이름
     * @return 방 참가 정보
     */
    public RoomDto getRoomDtoByName(String roomName) {
        String simpDestination = "/topic/thankcircle/"+roomName;
        List<Participant> participantList = participantService.getParticipant(simpDestination);

        ThankCircleRoom room = tcRoomRepository.findByName(roomName).orElseThrow();
        return new RoomDto(room.getName(), room.getName(), room.getShortUrl(), room.getQrCode(), participantList.size());
    }

    public RoomInfoMessage getRoomInfoByName(String roomName) {
        String simpDestination = "/topic/thankcircle/"+roomName;
        List<Participant> participantList = participantService.getParticipant(simpDestination);
        return new RoomInfoMessage(participantList.size());
    }

}
