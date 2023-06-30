package com.example.teamtwelvebackend.activity.thankcircle.controller.rest.response;

public record RoomResponse(
        String roomName,
        String roomCode,
        String qrCodeImageUrl,
        String shortUrl,
        int participantCount
) {
}
