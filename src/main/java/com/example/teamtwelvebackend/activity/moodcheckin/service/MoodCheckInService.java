package com.example.teamtwelvebackend.activity.moodcheckin.service;

import com.example.teamtwelvebackend.activity.moodcheckin.domain.MoodCheckInRoom;
import com.example.teamtwelvebackend.activity.moodcheckin.domain.MoodCheckInUserNickname;
import com.example.teamtwelvebackend.activity.moodcheckin.domain.RoomStatus;
import com.example.teamtwelvebackend.activity.moodcheckin.repository.MoodCheckInRoomRepository;
import com.example.teamtwelvebackend.activity.moodcheckin.repository.MoodCheckInUserNicknameRepository;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import com.example.teamtwelvebackend.ws.ActivityParticipant;
import com.example.teamtwelvebackend.ws.Participant;
import com.example.teamtwelvebackend.ws.ParticipantService;
import com.example.teamtwelvebackend.ws.RoomInfoMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MoodCheckInService {
    public static final String ACTIVITY_TYPE = "moodcheckin";

    private final MoodCheckInUserNicknameRepository moodCheckInUserNicknameRepository;
    private final MoodCheckInRoomRepository moodCheckInRoomRepository;

    private final ParticipantService participantService;

    @Transactional
    public MoodCheckInRoom createRoom(String userName) {
        MoodCheckInRoom moodCheckInRoom = MoodCheckInRoom.builder()
            .createdBy(userName)
            .build();
        return moodCheckInRoomRepository.save(moodCheckInRoom);
    }

    public MoodCheckInRoom getRoomByName(String roomName) {
        return moodCheckInRoomRepository.findByName(roomName)
            .orElseThrow(() -> new IllegalStateException("없는 방입니다."));
    }

    @Transactional
    public ActivityRoomMessage proceed(String roomName, String creatorId) {
        MoodCheckInRoom moodCheckInRoom = getRoomByName(roomName);

        if (!moodCheckInRoom.getCreatedBy().equals(creatorId)) {
            throw new IllegalStateException("방을 만든 사람이 아닙니다.");
        }
        RoomStatus status = moodCheckInRoom.next();
        switch (status) {
            case CREATED_ROOM -> {
                throw new IllegalStateException("초기 상태로 돌아올 수 없음");
            }
            case WAITING -> {
                List<ActivityParticipant> participants = participantService.getAll("/topic/moodcheckin/" + roomName);
                participants.forEach(participant -> registerName(roomName, participant.getNickname(), participant.getSessionId()));
                return new ActivityRoomMessage(status.toString(), "참여자를 기다리는 중", "");
            }
            case OPENED_AVERAGE -> {
                Double average = getAverage(roomName);
                return new ActivityRoomMessage(status.toString(), "제출한 기분의 평균 값", average);
            }
            case CLOSED_ROOM -> {
                getRandomGuest(roomName);
                return new ActivityRoomMessage(status.toString(), "", "{}");
            }
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    @Transactional
    public void registerName(String roomName, String userName, String sessionId) {
        MoodCheckInUserNickname moodCheckInUserNickname = MoodCheckInUserNickname.builder()
            .roomName(roomName)
            .nickname(userName)
            .sessionId(sessionId)
            .build();
        if (moodCheckInUserNicknameRepository.findBySessionId(sessionId).isPresent()) {
            throw new RuntimeException("이미 등록된 게스트입니다.");
        }
        moodCheckInUserNicknameRepository.save(moodCheckInUserNickname);
    }

    @Transactional
    public void updateMood(String mood, String sessionId) {
        MoodCheckInUserNickname findMoodCheckInUserNickname = moodCheckInUserNicknameRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 참여자입니다."));
        findMoodCheckInUserNickname.updateMood(Integer.valueOf(mood));
    }

    public ActivityRoomMessage getSubmitNumber(String roomName) {
        return new ActivityRoomMessage("임시타입", "현재 기분을 제출한 게스트 숫자", moodCheckInUserNicknameRepository.countByRoomNameAndMoodNot(roomName, 0));
    }

    public ActivityRoomMessage random(String roomName) {
        MoodCheckInUserNickname randomGuest = getRandomGuest(roomName);
        return new ActivityRoomMessage("OPENED_RANDOM", "제출한 기분의 랜덤 값", randomGuest);
    }

    private Double getAverage(String roomName) {
        return moodCheckInUserNicknameRepository.getAverageStatusForNonZero(roomName);
    }

    private MoodCheckInUserNickname getRandomGuest(String roomName) {
        return moodCheckInUserNicknameRepository.getRandomGuest(roomName);
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
        MoodCheckInRoom moodCheckInRoom = getRoomByName(roomName);

        if (!moodCheckInRoom.getCreatedBy().equals(creatorId)) {
            throw new IllegalStateException("방을 만든 사람이 아닙니다.");
        }

        RoomStatus status = moodCheckInRoom.close();
        return new ActivityRoomMessage(status.toString(), "", "{}");
    }

    public RoomInfoMessage getRoomInfoByName(String roomName) {
        String simpDestination = "/topic/%s/%s".formatted(ACTIVITY_TYPE, roomName);
        List<ActivityParticipant> participantList = participantService.getAll(simpDestination);
        return new RoomInfoMessage(participantList.size());
    }
}
