package com.example.teamtwelvebackend.activity.moodcheckin.controller.rest;

import com.example.teamtwelvebackend.activity.common.controller.response.ActivityCreateResponse;
import com.example.teamtwelvebackend.activity.common.controller.response.MoodCheckInRoomResponse;
import com.example.teamtwelvebackend.activity.moodcheckin.domain.MoodCheckInRoom;
import com.example.teamtwelvebackend.activity.moodcheckin.service.MoodCheckInService;
import com.example.teamtwelvebackend.qr.NaverShortUrlService;
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
@RequestMapping("/activity/moodcheckin")
public class MoodCheckInController {

    private final MoodCheckInService moodCheckInService;
    private final NaverShortUrlService naverShortUrlService;

    @PostMapping()
    public ResponseEntity<ActivityCreateResponse> createRoom(JwtAuthenticationToken principal) {
        String userName = principal.getName();// UUID, ex) 204c3264-77d5-4ac7-b776-4be9921535ee
        MoodCheckInRoom moodCheckInRoom = moodCheckInService.createRoom(userName);
        // TODO
        return ResponseEntity.ok().body(new ActivityCreateResponse(moodCheckInRoom.getName(), "sample-code"));
    }

    @GetMapping("/{roomName}")
    public ResponseEntity<MoodCheckInRoomResponse> getRoomInfo(@PathVariable String roomName) {
        MoodCheckInRoom roomByName = moodCheckInService.getRoomByName(roomName);
//        ShortURLAndQrVO shortURLAndQrCode = naverShortUrlService.createShortURLAndQrCode(
//            "http://api.bside1512.dev/activity/moodcheckin/" + roomName);
        MoodCheckInRoomResponse moodCheckInRoomResponse = MoodCheckInRoomResponse.builder()
            .roomName(roomByName.getName())
            .roomCode(roomByName.getName())
            .qrCodeImageUrl("qr-code")
            .shortUrl("short-url")
            .build();
        return ResponseEntity.ok().body(moodCheckInRoomResponse);
    }
}
