package com.example.teamtwelvebackend.activity.moodcheckin.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class MoodCheckInRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String createdBy;

    @Builder
    public MoodCheckInRoom(String createdBy) {
        this.name = UUID.randomUUID().toString();
        this.createdBy = createdBy;
    }
}
