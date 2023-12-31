package com.example.teamtwelvebackend.activity.speedgame.controller.rest.response;

public record SpeedGameRoomResponse(
        String roomName,
        String roomCode,
        String qrCodeImageUrl,
        String shortUrl,
        int participantCount
) {
}
