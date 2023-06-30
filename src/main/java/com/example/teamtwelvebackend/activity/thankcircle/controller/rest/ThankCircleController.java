package com.example.teamtwelvebackend.activity.thankcircle.controller.rest;

import com.example.teamtwelvebackend.activity.common.controller.response.ActivityCreateResponse;
import com.example.teamtwelvebackend.activity.thankcircle.controller.rest.response.RoomResponse;
import com.example.teamtwelvebackend.activity.thankcircle.service.TcGuestService;
import com.example.teamtwelvebackend.activity.thankcircle.service.TcHostService;
import com.example.teamtwelvebackend.activity.thankcircle.service.dto.RoomCreatedDto;
import com.example.teamtwelvebackend.activity.thankcircle.service.dto.RoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/activity/thankcircle")
public class ThankCircleController {
    final TcHostService service;
    final TcGuestService tcGuestService;

    @PostMapping
    public ResponseEntity<ActivityCreateResponse> createRoom(JwtAuthenticationToken principal) {
        String name = principal.getName();
        RoomCreatedDto room = service.createRoom(name);
        return ResponseEntity.ok().body(new ActivityCreateResponse(room.roomName(), room.roomCode()));
    }

    @GetMapping("/{roomName}")
    public ResponseEntity<RoomResponse> getRoomInfo(@PathVariable String roomName) {
        RoomDto room = tcGuestService.getRoomDtoByName(roomName);
        return ResponseEntity.ok().body(new RoomResponse(room.roomName(), room.roomCode(), room.qrUrl(), room.shortUrl(), room.participantCount()));
    }
}
