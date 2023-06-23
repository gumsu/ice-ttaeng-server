package com.example.teamtwelvebackend.activity.common.controller;

import com.example.teamtwelvebackend.activity.common.controller.response.ActivityResponse;
import com.example.teamtwelvebackend.activity.common.service.ActivityService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> list() {
        List<ActivityResponse> list = activityService.getAllActivity()
            .stream()
            .map(ActivityResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(list);
    }
}
