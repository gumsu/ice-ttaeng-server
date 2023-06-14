package com.example.teamtwelvebackend.activity.controller;

import com.example.teamtwelvebackend.activity.controller.request.SpeedGameCreateRequest;
import com.example.teamtwelvebackend.activity.controller.response.ActivityCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/activity/speedgame")
public class SpeedGameController {
    @PostMapping
    public ResponseEntity<ActivityCreateResponse> createRoom(JwtAuthenticationToken principal, @RequestBody SpeedGameCreateRequest request) {
        String username = principal.getToken().getClaim("username"); // username, ex) google_118339889321875083261
        String name = principal.getName();// UUID, ex) 204c3264-77d5-4ac7-b776-4be9921535ee
        return ResponseEntity.ok().body(new ActivityCreateResponse("sample-name", "sample-code"));
    }
}
