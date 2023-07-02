package com.example.teamtwelvebackend.activity.moodcheckin.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "mc_user_nickname")
public class MoodCheckInUserNickname {

    @Id
    @GeneratedValue
    private Long id;

    private String roomName;
    private String sessionId;
    private String nickname;
    private Integer mood;

    @Builder
    public MoodCheckInUserNickname(String roomName, String sessionId, String nickname) {
        this.roomName = roomName;
        this.sessionId = sessionId;
        this.nickname = nickname;
        this.mood = 0;
    }

    public void updateMood(Integer payload) {
        this.mood = payload;
    }
}
