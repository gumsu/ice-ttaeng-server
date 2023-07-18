package com.example.teamtwelvebackend.activity.mininetworking.controller.ws.message;

public record SubmitGroupingSize(String number) {

    public Integer getParseIntegerNumber() {
        return Integer.parseInt(number);
    }
}
