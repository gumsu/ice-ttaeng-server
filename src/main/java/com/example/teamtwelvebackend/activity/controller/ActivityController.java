package com.example.teamtwelvebackend.activity.controller;

import com.example.teamtwelvebackend.activity.controller.response.ActivityResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> list() {
        List<ActivityResponse> list = List.of(
                new ActivityResponse(1L, "스피드게임", "자유 퀴즈를 만들고 정답을 맞혀보세요"),
                new ActivityResponse(2L, "두 개의 진실 하나의 거짓말", "세 가지 정보 중 하나의 거짓을 찾아보세요")
        );
        return ResponseEntity.ok().body(list);
    }
}
