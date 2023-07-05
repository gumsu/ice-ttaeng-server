package com.example.teamtwelvebackend.activity.mininetworking.controller.rest;

import com.example.teamtwelvebackend.activity.common.controller.response.ActivityCreateResponse;
import com.example.teamtwelvebackend.activity.mininetworking.controller.rest.response.MiniNetworkingRoomResponse;
import com.example.teamtwelvebackend.activity.mininetworking.domain.MiniNetworkingRoom;
import com.example.teamtwelvebackend.activity.mininetworking.service.MiniNetworkingService;
import com.example.teamtwelvebackend.qr.NaverShortUrlService;
import com.example.teamtwelvebackend.qr.ShortURLAndQrVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/activity/mininetworking")
public class MiniNetworkingController {

    private final MiniNetworkingService miniNetworkingService;
    private final NaverShortUrlService naverShortUrlService;

    @PostMapping()
    public ResponseEntity<ActivityCreateResponse> createRoom(JwtAuthenticationToken principal) {
        String userName = principal.getName();
        MiniNetworkingRoom miniNetworkingRoom = miniNetworkingService.createRoom(userName);
        return ResponseEntity.ok().body(new ActivityCreateResponse(miniNetworkingRoom.getName(), "sample-code"));
    }

    @GetMapping("/{roomName}")
    public ResponseEntity<MiniNetworkingRoomResponse> getRoomInfo(@PathVariable String roomName) {
        MiniNetworkingRoom roomByName = miniNetworkingService.getRoomByName(roomName);
        ShortURLAndQrVO shortURLAndQrCode = naverShortUrlService.createShortURLAndQrCode("https://bside1512.dev/activity/mininetworking/" + roomName);
        MiniNetworkingRoomResponse miniNetworkingRoomResponse = MiniNetworkingRoomResponse.builder()
            .roomName(roomByName.getName())
            .roomCode(roomByName.getName())
            .qrCodeImageUrl(shortURLAndQrCode.getUrl())
            .shortUrl(shortURLAndQrCode.getQr())
            .build();
        return ResponseEntity.ok().body(miniNetworkingRoomResponse);
    }
}
