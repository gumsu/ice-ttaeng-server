package com.example.teamtwelvebackend.activity.service;

import com.example.teamtwelvebackend.activity.domain.MoodCheckIn;
import com.example.teamtwelvebackend.activity.repository.MoodCheckInRepository;
import com.example.teamtwelvebackend.activity.speedgame.controller.ws.message.ActivityRoomMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MoodCheckInService {

    private final MoodCheckInRepository moodCheckInRepository;


    public ActivityRoomMessage getContent(String roomName) {
        return new ActivityRoomMessage("타입", "메시지", "10명");
    }

    public void registerName(String payload, String sessionId) {
        MoodCheckIn moodCheckIn = MoodCheckIn.builder()
            .name(payload)
            .sessionId(sessionId)
            .build();
        // TODO 세션아이디 중복으로 저장하는지 체크 필요
        moodCheckInRepository.save(moodCheckIn);
    }

    public ActivityRoomMessage getNumber() {
        return new ActivityRoomMessage("임시타입", "임시메시지", moodCheckInRepository.count());
    }

    @Transactional
    public void updateMood(String payload, String sessionId) {
        MoodCheckIn findMoodCheckIn = moodCheckInRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 참여자입니다."));
        findMoodCheckIn.updateMood(Integer.valueOf(payload));
    }
}
