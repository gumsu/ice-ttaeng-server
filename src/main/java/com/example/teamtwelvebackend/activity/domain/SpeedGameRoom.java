package com.example.teamtwelvebackend.activity.domain;

import com.example.teamtwelvebackend.activity.controller.request.SpeedGameCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class SpeedGameRoom {
    @Id
    @GeneratedValue
    Long id;

    String name;

    @Enumerated(value = EnumType.STRING)
    SpeedGameRoomStatus status;

    Integer currentQuestion = 0;
    Integer totalQuestion = 5; // for test

    String createdBy;

    public SpeedGameRoom(String creatorId) {
        createdBy = creatorId;
        status = SpeedGameRoomStatus.CREATED_ROOM;
        name = UUID.randomUUID().toString();
    }

    /**
     * 액티비티 룸의 상태 전이를 위한 메소드
     *
     * @return
     */
    public SpeedGameRoomStatus next() {
        switch (status) {
            case CREATED_ROOM -> {
                status = SpeedGameRoomStatus.OPENED_QUESTION;
                currentQuestion++;
            }
            case OPENED_QUESTION -> {
                status = SpeedGameRoomStatus.OPENED_ANSWER;
            }
            case OPENED_ANSWER -> {
                if (currentQuestion + 1 > totalQuestion) {
                    status = SpeedGameRoomStatus.CLOSED_ROOM;
                } else {
                    status = SpeedGameRoomStatus.OPENED_QUESTION;
                    currentQuestion++;
                }
            }
            case CLOSED_ROOM -> throw new IllegalStateException("Cannot change status: already closed");
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
        return status;
    }
}
