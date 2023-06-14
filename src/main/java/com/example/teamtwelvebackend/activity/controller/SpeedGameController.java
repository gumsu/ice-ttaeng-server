package com.example.teamtwelvebackend.activity.controller;

import com.example.teamtwelvebackend.activity.controller.request.SpeedGameCreateRequest;
import com.example.teamtwelvebackend.activity.controller.response.ActivityCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/activity/speedgame")
public class SpeedGameController {
    @PostMapping
    public ResponseEntity<ActivityCreateResponse> createRoom(@RequestBody SpeedGameCreateRequest request) {
        return ResponseEntity.ok().body(new ActivityCreateResponse("sample-name", "sample-code"));
    }
}
