package com.example.teamtwelvebackend.activity.controller.response;

public record SpeedGameRoomResponse(
        String roomName,
        String roomCode,
        String qrCodeImageUrl, // TODO naver qr 이용
        String shortUrl        // TODO naver qr 이용
) {
}
