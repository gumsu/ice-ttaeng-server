package com.example.teamtwelvebackend.activity.domain;

import lombok.Getter;

@Getter
public class SpeedGameRoom {

    SpeedGameRoomStatus status = SpeedGameRoomStatus.CREATED_ROOM;
    Integer currentQuestion = 0;
    Integer totalQuestion = 30; // for test

    /**
     * 액티비티 룸의 상태 전이를 위한 메소드
     */
    public void next() {
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
            case CLOSED_ROOM -> {
            }
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
    }
}
