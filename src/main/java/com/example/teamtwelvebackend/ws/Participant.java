package com.example.teamtwelvebackend.ws;

import lombok.RequiredArgsConstructor;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Participant implements Principal {
    final String id;
    String nickname;

    Map<String, String> destinations = new HashMap<>();

    @Override
    public String getName() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
