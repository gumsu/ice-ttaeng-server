package com.example.teamtwelvebackend.activity.thankcircle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "tc_room")
public class ThankCircleRoom {
    @Id
    @GeneratedValue
    Long id;

    String name;

    @Enumerated(value = EnumType.STRING)
    TcRoomStatus status;

    Integer currentNumber = 0;
    Integer total;

    String createdBy;

    public ThankCircleRoom(String creatorId) {
        createdBy = creatorId;
        status = TcRoomStatus.CREATED_ROOM;
        name = UUID.randomUUID().toString();
    }

    /**
     * 액티비티 룸의 상태 전이를 위한 메소드
     *
     * @return
     */
    public TcRoomStatus next() {
        switch (status) {
            case CREATED_ROOM -> {
                // TODO 참가자가 한명일 경우 시작할 수 없음
                status = TcRoomStatus.READY;
                currentNumber++;
            }
            case READY -> {
                status = TcRoomStatus.GUIDE_THANKS_TO;
            }
            case GUIDE_THANKS_TO -> {
                if (currentNumber < total) {
                    currentNumber++;
                } else {
                    status = TcRoomStatus.CLOSED_ROOM;
                }
            }
            case CLOSED_ROOM -> throw new IllegalStateException("Cannot change status: already closed");
            default -> throw new IllegalStateException("Unexpected value: " + status);
        }
        return status;
    }

    public void updateTotalCount(int size) {
        this.total = size;
    }

    public TcRoomStatus close() {
        status = TcRoomStatus.CLOSED_ROOM;
        return status;
    }
}
