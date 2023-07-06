package com.example.teamtwelvebackend.ws;

import lombok.RequiredArgsConstructor;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Participant implements Principal {
    private final String id;
    String nickname;
    private String sessionId = null;

    Map<String, String> destinations = new HashMap<>();

    @Override
    public String getName() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }

    public void addDestination(String id, String destination) {
        destinations.put(id, destination);
    }

    public Map<String, String> getDestinations() {
        return destinations;
    }

    public String removeDestinationBySubscriptionId(String id) {
        String result = destinations.get(id);
        destinations.remove(id);
        return result;
    }
}
