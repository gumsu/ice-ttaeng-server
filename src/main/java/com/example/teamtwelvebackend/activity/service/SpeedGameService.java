package com.example.teamtwelvebackend.activity.service;

import com.example.teamtwelvebackend.activity.domain.Activity;
import com.example.teamtwelvebackend.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SpeedGameService {

    private final ActivityRepository activityRepository;

    public List<Activity> getAllActivity() {
        return activityRepository.findAll();
    }
}
