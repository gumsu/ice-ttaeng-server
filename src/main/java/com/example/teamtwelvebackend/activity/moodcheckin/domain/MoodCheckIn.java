package com.example.teamtwelvebackend.activity.moodcheckin.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MoodCheckIn {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String sessionId;
    private Integer status;
    private LocalDateTime localDateTime;

    @Builder
    public MoodCheckIn(String name, String sessionId) {
        this.name = name;
        this.sessionId = sessionId;
        this.status = 0;
        this.localDateTime = LocalDateTime.now();
    }

    public void updateMood(Integer payload) {
        this.status = payload;
    }
}
