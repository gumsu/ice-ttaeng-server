package com.example.teamtwelvebackend.activity.moodcheckin.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "mc_room")
public class MoodCheckInRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    RoomStatus status;

    private String createdBy;

    @Builder
    public MoodCheckInRoom(String createdBy) {
        this.name = UUID.randomUUID().toString();
        this.status = RoomStatus.CREATED_ROOM;
        this.createdBy = createdBy;
    }

    public RoomStatus next() {
        if (this.status.equals(RoomStatus.CLOSED_ROOM)) {
            throw new RuntimeException("Cannot change status: already closed");
        }

        int currentIndex = status.ordinal();
        RoomStatus[] values = RoomStatus.values();
        if (currentIndex < values.length - 1) {
            status = values[currentIndex + 1];
        }
        return status;
    }
}
