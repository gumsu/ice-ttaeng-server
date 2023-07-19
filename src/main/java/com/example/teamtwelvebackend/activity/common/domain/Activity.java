package com.example.teamtwelvebackend.activity.common.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Table(name = "activities")
@Getter
@Entity
public class Activity implements Comparable<Activity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "`order`")
    private Integer order;
    private String displayName;
    private String description;
    private LocalDateTime createdAt;

    public Activity(Long id, Integer order, String displayName, String description, LocalDateTime createdAt) {
        this.id = id;
        this.order = order;
        this.displayName = displayName;
        this.description = description;
        this.createdAt = createdAt;
    }

    @Override
    public int compareTo(Activity o) {
        return this.order.compareTo(o.order);
    }
}
