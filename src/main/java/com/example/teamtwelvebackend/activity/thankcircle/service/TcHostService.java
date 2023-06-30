package com.example.teamtwelvebackend.activity.thankcircle.service;

import com.example.teamtwelvebackend.CustomJwtAuthenticationToken;
import com.example.teamtwelvebackend.activity.thankcircle.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.activity.thankcircle.controller.ws.message.ThankListMessage;
import com.example.teamtwelvebackend.activity.thankcircle.controller.ws.message.ThankMessage;
import com.example.teamtwelvebackend.activity.thankcircle.domain.*;
import com.example.teamtwelvebackend.activity.thankcircle.repository.*;
import com.example.teamtwelvebackend.activity.thankcircle.service.dto.RoomCreatedDto;
import com.example.teamtwelvebackend.ws.Participant;
import com.example.teamtwelvebackend.ws.ParticipantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TcHostService {
    final TcRoomRepository tcRoomRepository;
    final TcThanksFromToRepository tcThanksFromToRepository;

    final ParticipantService participantService;


    /**
     * 주최자가 방을 만드는 기능
     *
     * @param creatorId 주최자 id (UUID)
     * @return
     */
    @Transactional
    public RoomCreatedDto createRoom(String creatorId) {

        ThankCircleRoom thankCircleRoom = tcRoomRepository.save(new ThankCircleRoom(creatorId));

        return new RoomCreatedDto(thankCircleRoom.getName(), thankCircleRoom.getName());
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
        ThankCircleRoom thankCircleRoom = tcRoomRepository.findByName(roomName).orElseThrow();
        if (!thankCircleRoom.getCreatedBy().equals(creatorId)) {
            throw new IllegalStateException("방을 만든 사람이 아닙니다.");
        }
        TcRoomStatus status = thankCircleRoom.next();
        switch (status) {
            case CREATED_ROOM -> {
                throw new IllegalStateException("초기 상태로 돌아올 수 없음");
            }
            case READY -> {
                // 메시지에 참가자(호스트 포함) 전체 목록을 보내야 함
                List<String> list = getParticipantList(roomName);
                generateThanksList(roomName, list);
                thankCircleRoom.updateTotalCount(list.size());
                tcRoomRepository.save(thankCircleRoom);

                ThankListMessage payload = new ThankListMessage(list);
                return new ActivityRoomMessage(status.toString(), "", payload);
            }
            case GUIDE_THANKS_TO -> {
                // 메시지에 누가 누구에게 감사를 전할지 안내해야 함
                ThankCircleFromTo thanksFromTo = tcThanksFromToRepository.findByRoomNameAndNumber(roomName, thankCircleRoom.getCurrentNumber()).orElseThrow();
                String thanksFrom = thanksFromTo.getThanksFrom();
                String thanksTo = thanksFromTo.getThanksTo();
                ThankMessage payload = new ThankMessage(thanksFrom, thanksTo);
                return new ActivityRoomMessage(status.toString(), "", payload);
            }
            case CLOSED_ROOM -> {
                return new ActivityRoomMessage(status.toString(), "", "{}");
            }
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    /**
     * 액티비티 종료
     *
     * @param creatorId
     * @param roomName
     * @return
     */
    @Transactional
    public ActivityRoomMessage close(String creatorId, String roomName) {
        ThankCircleRoom thankCircleRoom = tcRoomRepository.findByName(roomName).orElseThrow();
        if (!thankCircleRoom.getCreatedBy().equals(creatorId)) {
            throw new IllegalStateException("방을 만든 사람이 아닙니다.");
        }
        TcRoomStatus status = thankCircleRoom.close();
        return new ActivityRoomMessage(status.toString(), "", "{}");
    }


    private List<String> getParticipantList(String roomName) {
        String simpDestination = "/topic/thankcircle/"+roomName;
        List<Principal> all = participantService.getAll(simpDestination);
        return all.stream().map(principal -> {
            if (principal instanceof Participant participant) {
                return participant.getNickname();
            } else if (principal instanceof CustomJwtAuthenticationToken token) {
                return token.getNickname();
            } else {
                return principal.getName();
            }
        }).toList();
    }

    /**
     * 감사 목록을 만든다
     *
     * @param roomName 방 아이디
     * @param unmodifiableList 참가자 닉네임 목록
     */
    private void generateThanksList(String roomName, List<String> unmodifiableList) {
        ArrayList<String> participantList = new ArrayList<>(unmodifiableList);  // to modifiable list
        Collections.shuffle(participantList);

        List<ThankCircleFromTo> list = new ArrayList<>();
        for (int i = 0; i < participantList.size(); i++) {
            int number = i+1;
            int current = i;
            int next = (i+1 < participantList.size()) ? i+1 : 0;

            String thanksFrom = participantList.get(current);
            String thanksTo = participantList.get(next);

            ThankCircleFromTo thankCircleFromTo = new ThankCircleFromTo(roomName, number, thanksFrom, thanksTo);
            list.add(thankCircleFromTo);
        }
        tcThanksFromToRepository.saveAll(list);
    }
}
