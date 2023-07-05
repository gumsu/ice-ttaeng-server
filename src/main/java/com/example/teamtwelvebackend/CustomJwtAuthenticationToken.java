package com.example.teamtwelvebackend;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 기본 JwtAuthenticationToken 에서 추가 정보(nickname)를 다루기 위해 만들어진 클래스
 */
public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {

    private String nickname;

    Map<String, String> destinations = new HashMap<>();
    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name) {
        super(jwt, authorities, name);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
