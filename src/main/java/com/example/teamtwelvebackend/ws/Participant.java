package com.example.teamtwelvebackend.ws;

import lombok.RequiredArgsConstructor;

import javax.security.auth.Subject;
import java.security.Principal;

@RequiredArgsConstructor
public class Participant implements Principal {
    final String id;
    String nickname;

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
}
