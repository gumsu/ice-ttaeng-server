package com.example.teamtwelvebackend.activity.mininetworking.domain;


import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "mn_grouping")
public class MiniNetworkingGrouping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    private String groupName;

    @ElementCollection
    @CollectionTable(name = "mn_participants", joinColumns = @JoinColumn(name = "mn_grouping_id"))
    @MapKeyColumn(name = "participant_sessionId")
    @Column(name = "participant_name")
    private Map<String, String> participantSessionIdAndNickName = new HashMap<>();

    @Builder
    public MiniNetworkingGrouping(String roomName, String groupName, Map<String, String> participantSessionIdAndNickName) {
        this.roomName = roomName;
        this.groupName = groupName;
        this.participantSessionIdAndNickName = participantSessionIdAndNickName;
    }
}

