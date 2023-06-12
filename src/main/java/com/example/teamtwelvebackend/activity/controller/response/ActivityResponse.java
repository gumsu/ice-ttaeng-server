package com.example.teamtwelvebackend.activity.controller.response;

import lombok.Value;

@Value
public class ActivityResponse {
    Long activityId;
    String displayName;
    String description;
}
