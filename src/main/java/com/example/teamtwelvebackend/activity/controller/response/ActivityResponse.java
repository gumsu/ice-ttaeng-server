package com.example.teamtwelvebackend.activity.controller.response;

import com.example.teamtwelvebackend.activity.domain.Activity;
import lombok.Getter;

@Getter
public class ActivityResponse {
    private Long activityId;
    private String displayName;
    private String description;

    public ActivityResponse(Activity activity) {
        this.activityId = activity.getId();
        this.displayName = activity.getDisplayName();
        this.description = activity.getDescription();
    }
}
