package com.example.teamtwelvebackend.activity.mininetworking.service;

import com.example.teamtwelvebackend.activity.mininetworking.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.activity.mininetworking.controller.ws.message.SubmitGroupingSize;
import com.example.teamtwelvebackend.activity.mininetworking.domain.MiniNetworkingGrouping;
import com.example.teamtwelvebackend.activity.mininetworking.domain.MiniNetworkingRoom;
import com.example.teamtwelvebackend.activity.mininetworking.domain.RoomStatus;
import com.example.teamtwelvebackend.activity.mininetworking.repository.MiniNetworkingGroupingRepository;
import com.example.teamtwelvebackend.activity.mininetworking.repository.MiniNetworkingRoomRepository;
import com.example.teamtwelvebackend.ws.Participant;
import com.example.teamtwelvebackend.ws.ParticipantService;
import com.example.teamtwelvebackend.ws.RoomInfoMessage;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MiniNetworkingService {
    public static final String ACTIVITY_TYPE = "mininetworking";

    private final MiniNetworkingRoomRepository miniNetworkingRoomRepository;
    private final MiniNetworkingGroupingRepository miniNetworkingGroupingRepository;

    private final SimpMessagingTemplate messagingTemplate;
    private final ParticipantService participantService;

    @Transactional
    public MiniNetworkingRoom createRoom(String userName) {
        MiniNetworkingRoom miniNetworkingRoom = MiniNetworkingRoom.builder()
            .createdBy(userName)
            .build();
        return miniNetworkingRoomRepository.save(miniNetworkingRoom);
    }

    public MiniNetworkingRoom getRoomByName(String roomName) {
        return miniNetworkingRoomRepository.findByName(roomName)
            .orElseThrow(() -> new IllegalStateException("없는 방입니다."));
    }

    @Transactional
    public ActivityRoomMessage proceed(String roomName, String creatorId) {
        MiniNetworkingRoom miniNetworkingRoom = getRoomByName(roomName);

        if (!miniNetworkingRoom.getCreatedBy().equals(creatorId)) {
            throw new IllegalStateException("방을 만든 사람이 아닙니다.");
        }

        RoomStatus status = miniNetworkingRoom.next();
        switch (status) {
            case CREATED_ROOM -> {
                throw new IllegalStateException("초기 상태로 돌아올 수 없음");
            }
            case OPENED_PARTICIPANT_LIST -> {
                List<Participant> participants = getParticipantList(roomName);
                List<String> participantList = participants.stream()
                    .map(Participant::getNickname)
                    .collect(Collectors.toList());
                return new ActivityRoomMessage(status.toString(), "", participantList);
            }
            case GROUPING -> {
                // submit 으로 몇 명인지 따로 받으면서 next() 함수 호출해서 각 참여자에게 그룹 알람 전파
                List<MiniNetworkingGrouping> findByRoomNames = miniNetworkingGroupingRepository.findByRoomName(roomName);
                for (MiniNetworkingGrouping findByRoomName : findByRoomNames) {
                    Map<String, String> participantSessionIdAndNickName = findByRoomName.getParticipantSessionIdAndNickName();

                    for (Entry<String, String> entrySet : participantSessionIdAndNickName.entrySet()) {
                        System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
                        Map<String, String> map = new HashMap<>();
                        map.put("nickname", entrySet.getValue());
                        map.put("group", findByRoomName.getGroupName());
                        ActivityRoomMessage message = new ActivityRoomMessage("GROUPING_RESULT", "", map);
                        messagingTemplate.convertAndSendToUser(entrySet.getKey(),"/queue/reply", message);
                    }
                }
                return new ActivityRoomMessage(status.toString(), "", "");
            }
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    @Transactional
    public void createParticipantGroups(String roomName, SubmitGroupingSize number) {
        List<Participant> participants = getParticipantList(roomName);
        int totalParticipant = participants.size();
        int numberOfGroup = number.getParseIntegerNumber(); // 입력 값은 그룹의 갯수
        int remainder = totalParticipant % number.getParseIntegerNumber();
        int groupSize = totalParticipant / number.getParseIntegerNumber(); // 그룹의 크기는 참가자 수와 그룹의 갯수에 따라 균등하지 않을 수 있따

        int startIndex = 0;
        int endIndex;

        for (int i = 0; i < remainder; i++) {
            endIndex = startIndex + groupSize + 1;

            List<Participant> participantsubList = participants.subList(startIndex, endIndex);
            for (Participant participant : participantsubList) {
                HashMap<String, String> participantSessionIdAndNickName = new HashMap<>();
                participantSessionIdAndNickName.put(participant.getName(), participant.getNickname());

                MiniNetworkingGrouping miniNetworkingGrouping = MiniNetworkingGrouping.builder()
                    .roomName(roomName)
                    .groupName(String.valueOf(i + 1))
                    .participantSessionIdAndNickName(participantSessionIdAndNickName)
                    .build();
                miniNetworkingGroupingRepository.save(miniNetworkingGrouping);
            }
            startIndex = endIndex;
        }

        for (int i = remainder; i < numberOfGroup; i++) {
            endIndex = startIndex + groupSize;

            List<Participant> participantsubList = participants.subList(startIndex, endIndex);
            for (Participant participant : participantsubList) {
                HashMap<String, String> participantSessionIdAndNickName = new HashMap<>();
                participantSessionIdAndNickName.put(participant.getName(), participant.getNickname());

                MiniNetworkingGrouping miniNetworkingGrouping = MiniNetworkingGrouping.builder()
                    .roomName(roomName)
                    .groupName(String.valueOf(i + 1))
                    .participantSessionIdAndNickName(participantSessionIdAndNickName)
                    .build();
                miniNetworkingGroupingRepository.save(miniNetworkingGrouping);
            }
            startIndex = endIndex;
        }

    }

    private List<Participant> getParticipantList(String roomName) {
        return participantService.getParticipant("/topic/mininetworking/" + roomName);
    }

    public RoomInfoMessage getRoomInfoByName(String roomName) {
        int count = getParticipantList(roomName).size();
        return new RoomInfoMessage(count);
    }
}
