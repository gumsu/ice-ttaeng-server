package com.example.teamtwelvebackend.ws;

import java.security.Principal;
import java.util.Map;

public interface ActivityParticipant extends Principal {

    void setNickname(String nickname);
    void addDestination(String subscriptionId, String destination);
    void setSessionId(String sessionId);

    Map<String, String> getDestinations();

    String getNickname();

    String removeDestinationBySubscriptionId(String subscriptionId);
}
