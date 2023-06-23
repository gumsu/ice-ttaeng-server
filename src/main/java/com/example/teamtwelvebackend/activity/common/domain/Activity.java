package com.example.teamtwelvebackend.activity.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Table(name = "activities")
@Getter
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String displayName;
    private String description;
    private LocalDateTime createdAt;

    public Activity(Long id, String displayName, String description, LocalDateTime createdAt) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.createdAt = createdAt;
    }
}
