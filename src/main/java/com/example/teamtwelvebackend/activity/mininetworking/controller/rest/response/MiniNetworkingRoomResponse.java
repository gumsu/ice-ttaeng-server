package com.example.teamtwelvebackend.activity.mininetworking.controller.rest.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MiniNetworkingRoomResponse {

    private String roomName;
    private String roomCode;
    private String qrCodeImageUrl;
    private String shortUrl;

    @Builder
    public MiniNetworkingRoomResponse(String roomName, String roomCode, String qrCodeImageUrl, String shortUrl) {
        this.roomName = roomName;
        this.roomCode = roomCode;
        this.qrCodeImageUrl = qrCodeImageUrl;
        this.shortUrl = shortUrl;
    }
}
