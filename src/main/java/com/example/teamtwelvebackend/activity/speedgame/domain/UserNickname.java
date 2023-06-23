package com.example.teamtwelvebackend.activity.speedgame.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "sg_user_nickname")
public class UserNickname {
    @Id
    @GeneratedValue
    Long id;
    String roomName;
    String sessionId;
    String nickname;

    public UserNickname(String roomName, String sessionId, String nickname) {
        this.roomName = roomName;
        this.sessionId = sessionId;
        this.nickname = nickname;
    }
}
