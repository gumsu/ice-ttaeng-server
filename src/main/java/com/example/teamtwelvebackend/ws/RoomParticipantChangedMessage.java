package com.example.teamtwelvebackend.ws;

public record RoomParticipantChangedMessage(

        String nickname,
        int currentParticipantCount
) {
}
