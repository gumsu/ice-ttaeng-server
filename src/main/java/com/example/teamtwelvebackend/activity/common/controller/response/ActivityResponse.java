package com.example.teamtwelvebackend.activity.common.controller.response;

import com.example.teamtwelvebackend.activity.common.domain.Activity;
import lombok.Getter;

@Getter
public class ActivityResponse {
    private Long activityId;
    private String displayName;
    private String description;
    private Integer numberOfPerson;

    public ActivityResponse(Activity activity) {
        this.activityId = activity.getId();
        this.displayName = activity.getDisplayName();
        this.description = activity.getDescription();
        this.numberOfPerson = activity.getNumberOfPerson();
    }
}
