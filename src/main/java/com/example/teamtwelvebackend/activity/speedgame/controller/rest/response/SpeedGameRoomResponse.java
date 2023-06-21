package com.example.teamtwelvebackend.activity.speedgame.controller.rest.response;

public record SpeedGameRoomResponse(
        String roomName,
        String roomCode,
        String qrCodeImageUrl, // TODO naver qr 이용
        String shortUrl        // TODO naver qr 이용
) {
}
