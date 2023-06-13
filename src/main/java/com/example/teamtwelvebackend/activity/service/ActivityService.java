package com.example.teamtwelvebackend.activity.service;

import com.example.teamtwelvebackend.activity.domain.Activity;
import com.example.teamtwelvebackend.activity.repository.ActivityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public List<Activity> getAllActivity() {
        return activityRepository.findAll();
    }
}
