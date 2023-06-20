package com.example.teamtwelvebackend.activity.controller;

import com.example.teamtwelvebackend.activity.controller.request.SpeedGameCreateRequest;
import com.example.teamtwelvebackend.activity.controller.response.ActivityCreateResponse;
import com.example.teamtwelvebackend.activity.controller.response.SpeedGameRoomResponse;
import com.example.teamtwelvebackend.activity.service.SpeedGameCreatedDto;
import com.example.teamtwelvebackend.activity.service.SpeedGameRoomDto;
import com.example.teamtwelvebackend.activity.service.SpeedGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/activity/speedgame")
public class SpeedGameController {
    final SpeedGameService speedGameService;

    @PostMapping
    public ResponseEntity<ActivityCreateResponse> createRoom(JwtAuthenticationToken principal,
                                                             @RequestBody SpeedGameCreateRequest request) {
        String name = principal.getName();// UUID, ex) 204c3264-77d5-4ac7-b776-4be9921535ee
        SpeedGameCreatedDto room = speedGameService.createRoom(name, request);
        return ResponseEntity.ok().body(new ActivityCreateResponse(room.roomName(), room.roomCode()));
    }

    @GetMapping("/{roomName}")
    public ResponseEntity<SpeedGameRoomResponse> getRoomInfo(@PathVariable String roomName) {
        SpeedGameRoomDto room = speedGameService.getRoomByName(roomName);
        return ResponseEntity.ok().body(new SpeedGameRoomResponse(room.roomName(), room.roomCode(), "https://qrimaagelink", "https://shortlink"));
    }
}
