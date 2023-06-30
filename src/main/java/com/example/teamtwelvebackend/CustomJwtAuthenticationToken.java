package com.example.teamtwelvebackend;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

/**
 * 기본 JwtAuthenticationToken 에서 추가 정보(nickname)를 다루기 위해 만들어진 클래스
 */
public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {

    private String nickname;

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name) {
        super(jwt, authorities, name);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
