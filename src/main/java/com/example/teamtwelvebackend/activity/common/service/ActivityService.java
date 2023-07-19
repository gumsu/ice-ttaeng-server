package com.example.teamtwelvebackend.activity.common.service;

import com.example.teamtwelvebackend.activity.common.domain.Activity;
import com.example.teamtwelvebackend.activity.common.repository.ActivityRepository;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public List<Activity> getAllActivity() {
        return activityRepository.findAll().stream().sorted().toList();
    }
}
