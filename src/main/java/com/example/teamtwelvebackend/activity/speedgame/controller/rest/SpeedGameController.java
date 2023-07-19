package com.example.teamtwelvebackend.activity.speedgame.controller.rest;

import com.example.teamtwelvebackend.activity.common.controller.response.ActivityCreateResponse;
import com.example.teamtwelvebackend.activity.speedgame.controller.rest.request.SpeedGameCreateRequest;
import com.example.teamtwelvebackend.activity.speedgame.controller.rest.response.SpeedGameRoomResponse;
import com.example.teamtwelvebackend.activity.speedgame.service.GuestService;
import com.example.teamtwelvebackend.activity.speedgame.service.HostService;
import com.example.teamtwelvebackend.activity.speedgame.service.dto.RoomCreatedDto;
import com.example.teamtwelvebackend.activity.speedgame.service.dto.RoomDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/activity/speedgame")
public class SpeedGameController {
    final HostService service;
    final GuestService guestService;

    @PostMapping
    public ResponseEntity<ActivityCreateResponse> createRoom(JwtAuthenticationToken principal,
                                                             @Valid @RequestBody SpeedGameCreateRequest request) {
        String name = principal.getName();// UUID, ex) 204c3264-77d5-4ac7-b776-4be9921535ee
        RoomCreatedDto room = service.createRoom(name, request);
        return ResponseEntity.ok().body(new ActivityCreateResponse(room.roomName(), room.roomCode()));
    }

    @GetMapping("/{roomName}")
    public ResponseEntity<SpeedGameRoomResponse> getRoomInfo(@PathVariable String roomName) {
        RoomDto room = guestService.getRoomDtoByName(roomName);
        return ResponseEntity.ok().body(new SpeedGameRoomResponse(room.roomName(), room.roomCode(), room.qrUrl(), room.shortUrl(), room.participantCount()));
    }
}
