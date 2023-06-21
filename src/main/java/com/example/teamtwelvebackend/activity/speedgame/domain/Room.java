package com.example.teamtwelvebackend.activity.speedgame.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue
    Long id;

    String name;

    @Enumerated(value = EnumType.STRING)
    RoomStatus status;

    Integer currentQuestion = 0;
    Integer totalQuestion = 5; // for test

    String createdBy;

    public Room(String creatorId) {
        createdBy = creatorId;
        status = RoomStatus.CREATED_ROOM;
        name = UUID.randomUUID().toString();
    }

    /**
     * 액티비티 룸의 상태 전이를 위한 메소드
     *
     * @return
     */
    public RoomStatus next() {
        switch (status) {
            case CREATED_ROOM -> {
                status = RoomStatus.OPENED_QUESTION;
                currentQuestion++;
            }
            case OPENED_QUESTION -> {
                status = RoomStatus.OPENED_ANSWER;
            }
            case OPENED_ANSWER -> {
                if (currentQuestion + 1 > totalQuestion) {
                    status = RoomStatus.CLOSED_ROOM;
                } else {
                    status = RoomStatus.OPENED_QUESTION;
                    currentQuestion++;
                }
            }
            case CLOSED_ROOM -> throw new IllegalStateException("Cannot change status: already closed");
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
        return status;
    }
}
