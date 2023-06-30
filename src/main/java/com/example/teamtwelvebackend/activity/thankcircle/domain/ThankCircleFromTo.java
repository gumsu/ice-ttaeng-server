package com.example.teamtwelvebackend.activity.thankcircle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "tc_thanks_from_to")
public class ThankCircleFromTo {
    @Id
    @GeneratedValue
    Long id;

    String roomName;

    Integer number;

    String thanksFrom;
    String thanksTo;

    public ThankCircleFromTo(String roomName, Integer number, String thanksFrom, String thanksTo) {
        this.roomName = roomName;
        this.number = number;
        this.thanksFrom = thanksFrom;
        this.thanksTo = thanksTo;
    }
}
